package com.tip.capstone.mlearning.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
@SuppressWarnings("WeakerAccess")
public interface MainView extends MvpView {
    // let view public for DataBinding uses

    void onStudyClicked();

    void onAssessmentClicked();

    void onGradesClicked();

    // TODO: 24/11/2016 implements videos activity
    void onVideosClicked();

    // TODO: 24/11/2016 implements simulation activity
    void onSimulationClicked();

    // TODO: 24/11/2016 implements glossary activity
    void onGlossaryClicked();
}
