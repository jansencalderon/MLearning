package com.tip.capstone.mlearning.ui.grades.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.AssessmentGrade;
import com.tip.capstone.mlearning.model.Grades;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

@SuppressWarnings("WeakerAccess")
public interface GradesDetailView extends MvpView {
    void showDialog();

    void showGradeHistory(Grades grade);

    void showAssessmentHistory(Grades grades);

    // make view public for DataBinding uses
    // TODO: 24/11/2016 Ready for future updates
}
