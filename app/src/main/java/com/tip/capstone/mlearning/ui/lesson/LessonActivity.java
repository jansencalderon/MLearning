package com.tip.capstone.mlearning.ui.lesson;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityLessonBinding;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.model.PreQuizGrade;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.quiz.QuizActivity;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
public class LessonActivity extends MvpActivity<LessonView, LessonPresenter>
        implements LessonView, ViewPager.OnPageChangeListener {

    private static final String TAG = LessonActivity.class.getSimpleName();
    private ActivityLessonBinding binding;
    private Realm realm;

    private int dotsCount;
    private ImageView[] dots;
    private LessonPageAdapter lessonPageAdapter;
    private Topic topic;
    private List<Lesson> lessons;
    private int lessonDetailRefId;

    @SuppressWarnings("ConstantConditions") // assumes that toolbar is setup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance(); // init realm
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // checking for intent pass
        int topicId = getIntent().getIntExtra(Constant.ID, -1);
        lessonDetailRefId = getIntent().getIntExtra("lesson_detail_ref_id", -1);
        if (lessonDetailRefId != -1) {
            topic = realm.where(Topic.class).equalTo("lessons.lessondetails.id", lessonDetailRefId).findFirst();
            if (topic != null)
                topicId = topic.getId();
        }
        if (topicId == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        //check if has data
        topic = realm.where(Topic.class).equalTo(Constant.ID, topicId).findFirst();
        if (topic == null) {
            Toast.makeText(getApplicationContext(), "No Topic Object Found", Toast.LENGTH_SHORT).show();
            finish();
        }


        getSupportActionBar().setTitle(topic.getTitle());

        lessonPageAdapter = new LessonPageAdapter(getSupportFragmentManager(), topicId, topic.getVideo(), lessonDetailRefId);
        binding.container.addOnPageChangeListener(this);
        binding.container.setAdapter(lessonPageAdapter);

        setUiPageViewController();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 07/12/2016 save instance to check if objective is already displayed
        PreQuizGrade preQuizGrade = realm.where(PreQuizGrade.class).equalTo(Constant.ID, topic.getId()).findFirst();
        if (preQuizGrade == null) {
            showPreQuizPrompt();
        } else {
            showObjectives();
        }
    }

    private void showPreQuizPrompt() {
        new AlertDialog.Builder(this)
                .setTitle(topic.getTitle())
                .setMessage("You have to take the Pre-Assessment Quiz")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(LessonActivity.this, QuizActivity.class);
                        intent.putExtra(Constant.ID, topic.getId());
                        intent.putExtra(Constant.PRE_QUIZ, true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        LessonActivity.this.finish();
                    }
                })
                .show();
    }

    /**
     * Show the Topic Objective on Dialog
     */
    private void showObjectives() {
        new AlertDialog.Builder(this)
                .setTitle(topic.getTitle())
                .setMessage("Objective:\n" + topic.getObjective())
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public LessonPresenter createPresenter() {
        return new LessonPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                try {
                    RealmResults<Lesson> lessonRealmResults = topic.getLessons().where()
                            .contains("lessondetails.body", query, Case.INSENSITIVE).findAllSorted(Lesson.COL_SEQ);
                    RealmResults<LessonDetail> lessonDetail = lessonRealmResults.first()
                            .getLessondetails().where().contains("body", query, Case.INSENSITIVE)
                            .findAllSorted(LessonDetail.COL_SEQ);
                    lessonPageAdapter.setQuery(query, lessonDetail.first().getId());

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Toast.makeText(LessonActivity.this, "No results were found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                if (newText.isEmpty()) {
                    lessonPageAdapter.setQuery(null, -1);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            /*case R.id.action_quiz:
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra(Constant.ID, topic.getId());
                startActivity(intent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // for the view page counter/dots below the layout
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUiPageViewController() {
        // setup view page controller specially the counter indicator
        lessons = realm.copyFromRealm(topic.getLessons().sort(Lesson.COL_SEQ));
        lessonPageAdapter.setLessonList(lessons);
        dotsCount = lessonPageAdapter.getCount();
        if (dotsCount <= 0) return;
        dots = new ImageView[dotsCount];
        binding.viewPagerCountDots.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            binding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot));

        for (int i = 0; i < lessons.size(); i++) {
            for (int j = 0; j < lessons.get(i).getLessondetails().size(); j++) {
                if (lessons.get(i).getLessondetails().get(j).getId() == lessonDetailRefId) {
                    binding.container.setCurrentItem(i);
                    return;
                }
            }
        }

    }

}