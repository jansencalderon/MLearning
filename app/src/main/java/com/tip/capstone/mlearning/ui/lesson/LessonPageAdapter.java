package com.tip.capstone.mlearning.ui.lesson;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.ui.lesson.detail.LessonDetailListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

class LessonPageAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = LessonPageAdapter.class.getSimpleName();
    private final List<Lesson> lessonList;
    private final int topicId;
    private String query;
    private int firstLessonId;
    private String videoRawName;
    private int lessonDetailId;

    LessonPageAdapter(FragmentManager fm, int topicId, String videoRawName, int lessonDetailId) {
        super(fm);
        this.topicId = topicId;
        this.videoRawName = videoRawName;
        this.lessonDetailId = lessonDetailId;
        lessonList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + query);
        return LessonDetailListFragment.newInstance(topicId, lessonList.get(position).getId(),
                position == (lessonList.size() - 1), query, firstLessonId, videoRawName, lessonDetailId);
    }

    @Override
    public int getCount() {
        return lessonList.size();
    }

    void setLessonList(List<Lesson> lessonList) {
        this.lessonList.clear();
        this.lessonList.addAll(lessonList);
        notifyDataSetChanged();
    }

    void setQuery(String query, int firstLessonId) {
        this.query = query;
        this.firstLessonId = firstLessonId;
        Log.d(TAG, "setQuery: " + query);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}