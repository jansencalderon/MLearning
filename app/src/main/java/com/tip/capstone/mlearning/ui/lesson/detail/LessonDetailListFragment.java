package com.tip.capstone.mlearning.ui.lesson.detail;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.FragmentLessonBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.ui.lesson.LessonView;
import com.tip.capstone.mlearning.ui.quiz.QuizActivity;
import com.tip.capstone.mlearning.ui.videos.play.VideoPlayActivity;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
public class LessonDetailListFragment
        extends MvpFragment<LessonDetailListView, LessonDetailListPresenter>
        implements LessonDetailListView, TextToSpeech.OnInitListener {

    private static final String ARG_TOPIC_ID = "arg-topic-id";
    private static final String ARG_LESSON_ID = "arg-lesson-id";
    private static final String ARG_IS_LAST = "arg-is-last";
    private static final String ARG_QUERY = "arg-query";
    private static final String ARG_FIRST_INST = "arg-first-inst";
    private static final String TAG = LessonDetailListFragment.class.getSimpleName();
    private static final String ARG_VIDEO = "arg-video";
    private static final String ARG_DETAIL_REF = "arg-detail-ref";

    private int topicId;
    private int lessonId;
    private boolean isLastPage;
    private String query;
    private String videoRawName;
    private int lessonDetailRef;

    private Realm realm;
    private FragmentLessonBinding binding;
    private TextToSpeech textToSpeech;
    private int firstLessonId;
    private LinearLayoutManager layoutManager;
    private LessonView lessonViewCallback;

    /**
     * Create New Instance of the fragment with the parameters as Bundle
     *
     * @param lessonId       lesson id to be diplay on the fragment
     * @param firstLessonId
     * @param lessonDetailId
     * @return new instance of LessonDetailFragment
     */
    public static LessonDetailListFragment newInstance(int topicId, int lessonId, boolean isLastPage,
                                                       String query, int firstLessonId,
                                                       String videoRawName, int lessonDetailId) {
        LessonDetailListFragment fragment = new LessonDetailListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOPIC_ID, topicId);
        args.putInt(ARG_LESSON_ID, lessonId);
        args.putBoolean(ARG_IS_LAST, isLastPage);
        args.putString(ARG_QUERY, query);
        args.putInt(ARG_FIRST_INST, firstLessonId);
        args.putString(ARG_VIDEO, videoRawName);
        args.putInt(ARG_DETAIL_REF, lessonDetailId);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: query: " + query);
        return fragment;
    }

    @NonNull
    @Override
    public LessonDetailListPresenter createPresenter() {
        return new LessonDetailListPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retrieving parameters/bundle/arguments
        if (getArguments() != null) {
            topicId = getArguments().getInt(ARG_TOPIC_ID, -1);
            lessonId = getArguments().getInt(ARG_LESSON_ID, -1);
            isLastPage = getArguments().getBoolean(ARG_IS_LAST, false);
            query = getArguments().getString(ARG_QUERY);
            firstLessonId = getArguments().getInt(ARG_FIRST_INST, -1);
            videoRawName = getArguments().getString(ARG_VIDEO, null);
            lessonDetailRef = getArguments().getInt(ARG_DETAIL_REF, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // init data
        realm = Realm.getDefaultInstance();
        Lesson lesson = realm.where(Lesson.class).equalTo(Constant.ID, lessonId).findFirst();
        LessonDetailListAdapter lessonDetailListAdapter = new LessonDetailListAdapter(lesson, getMvpView(), isLastPage, query, lessonDetailRef);
        binding.recyclerView.setAdapter(lessonDetailListAdapter);
        List<LessonDetail> lessonDetails = realm.copyFromRealm(lesson.getLessondetails().sort(LessonDetail.COL_SEQ));
        lessonDetailListAdapter.setLessonDetails(lessonDetails);
        textToSpeech = new TextToSpeech(getContext(), this);
        Log.d(TAG, "onStart: " + firstLessonId);
        for (int i = 0; i < lessonDetails.size(); i++) {
            if (lessonDetails.get(i).getId() == firstLessonId) {
                binding.recyclerView.scrollToPosition(i);
                return;
            }
        }
    }

    @Override
    public void onStop() {
        // Don't forget to shutdown textToSpeech!
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        realm.close(); // close realm
        super.onStop();
    }

    @Override
    public void onDetailClick(final LessonDetail lessonDetail) {
        new AlertDialog.Builder(getContext())
                .setTitle("Text-to-Speech")
                .setMessage("Listen to the selected Lesson?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(lessonDetail.getBody(), TextToSpeech.QUEUE_FLUSH, null, lessonDetail.getId() + "");
                        } else {
                            //noinspection deprecation
                            textToSpeech.speak(lessonDetail.getBody(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onQuiz() {
        Intent intent = new Intent(getContext(), QuizActivity.class);
        intent.putExtra(Constant.ID, topicId);
        startActivity(intent);
    }

    @Override
    public void onViewVideo() {
        if (videoRawName != null) {
            Intent intent = new Intent(getContext(), VideoPlayActivity.class);
            intent.putExtra(Constant.RES_ID, ResourceHelper.getRawResourceId(getContext(), videoRawName));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No Video Name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void imageZoom(LessonDetail lessonDetail) {

        startActivity(new Intent(getActivity(), ZoomActivity.class).putExtra("pic",lessonDetail.getBody()));

    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Snackbar.make(binding.getRoot(), "The TextToSpeech Language is not Supported",
                        Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(binding.getRoot(), "TextToSpeech Initialization Failed!",
                    Snackbar.LENGTH_SHORT).show();
            Log.e("TTS", "Initilization Failed!");
        }
    }

}