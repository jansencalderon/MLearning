package com.tip.capstone.mlearning.ui.lesson.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.LessonDetail;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
@SuppressWarnings("WeakerAccess")
public interface LessonDetailListView extends MvpView {

    /**
     * Listener for Item Clicked
     *
     * @param lessonDetail item clicked
     */
    void onDetailClick(LessonDetail lessonDetail);

    void onQuiz();

    void onViewVideo();

    void imageZoom(LessonDetail lessonDetail);

}