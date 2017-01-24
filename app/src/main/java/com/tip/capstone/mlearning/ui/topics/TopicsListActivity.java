package com.tip.capstone.mlearning.ui.topics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityTopicsListBinding;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.lesson.LessonActivity;

import io.realm.Realm;

public class TopicsListActivity extends MvpActivity<TopicListView, TopicListPresenter>
        implements TopicListView {

    private Realm realm;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        ActivityTopicsListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_topics_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int termId = getIntent().getIntExtra(Constant.ID, -1);
        if (termId == -1) {
            Toast.makeText(this, "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        Term term = realm.where(Term.class).equalTo(Constant.ID, termId).findFirst();
        if (term == null) {
            Toast.makeText(this, "No Term Object Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(term.getTitle());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        TopicListAdapter topicListAdapter = new TopicListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(topicListAdapter);

        topicListAdapter.setTopicList(term.getTopics().sort(Topic.COL_SEQ));
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public TopicListPresenter createPresenter() {
        return new TopicListPresenter();
    }

    @Override
    public void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void onTopicClicked(Topic topic) {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra(Constant.ID, topic.getId());
        startActivity(intent);
    }
}
