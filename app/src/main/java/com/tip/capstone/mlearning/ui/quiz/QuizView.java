package com.tip.capstone.mlearning.ui.quiz;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.views.IdentificationView;

import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
@SuppressWarnings("WeakerAccess")
public interface QuizView extends IdentificationView {

    // let view public for DataBinding uses

    /**
     * Setup previous question
     */
    void onPrevious();

    /**
     * Setup next question
     */
    void onNext();

    /**
     * Restore Data after orientation change
     *
     * @param counter        current counter
     * @param questionList   question list with the applied sorting algorithm
     * @param userAnswerList current user answers
     */
    void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList, List<List<Letter>> lettersList);

}
