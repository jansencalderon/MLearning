package com.tip.capstone.mlearning.ui.grades;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Sen on 2/21/2017.
 */

public interface GradesView extends MvpView {
    void Prelim();

    void Midterm();

    void Finals();

    void All();
}
