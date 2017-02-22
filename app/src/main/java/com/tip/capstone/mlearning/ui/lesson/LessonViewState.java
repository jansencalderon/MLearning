package com.tip.capstone.mlearning.ui.lesson;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/22/2017.
 */

public class LessonViewState implements RestorableViewState<LessonView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<LessonView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(LessonView view, boolean retained) {

    }
}
