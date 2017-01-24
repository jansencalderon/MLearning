package com.tip.capstone.mlearning.ui.grades;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

@SuppressWarnings("WeakerAccess")
public interface GradesView extends MvpView {
    void showDialog();

    // make view public for DataBinding uses
    // TODO: 24/11/2016 Ready for future updates
}
