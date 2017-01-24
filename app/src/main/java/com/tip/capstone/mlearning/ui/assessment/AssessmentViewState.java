package com.tip.capstone.mlearning.ui.assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.tip.capstone.mlearning.app.App;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @see com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState
 * @see AssessmentView
 * ViewState implementation for AssessmentView
 * @since 22/11/2016
 */

class AssessmentViewState implements RestorableViewState<AssessmentView> {

    private static final String KEY_COUNTER = "key_counter";
    private static final String KEY_ANSWER = "key_user_answer_list";
    private static final String KEY_ASSESSMENT = "key_assessment_list";
    private static final String KEY_LETTERS = "key_letters_list";

    private int counter;
    private List<UserAnswer> userAnswerList;
    private List<Assessment> assessmentList;
    private List<List<Letter>> lettersList;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_COUNTER, counter);
        //convert object list to JSON String to be put inside the bundle
        Gson gson = new GsonBuilder().create();

        ArrayList<String> questionJson = new ArrayList<>();
        for (Assessment question : assessmentList) questionJson.add(gson.toJson(question));
        out.putStringArrayList(KEY_ASSESSMENT, questionJson);

        ArrayList<String> userAnswerJson = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswerList) userAnswerJson.add(gson.toJson(userAnswer));
        out.putStringArrayList(KEY_ANSWER, userAnswerJson);

        ArrayList<String> lettersChoiceJson = new ArrayList<>();
        for (List<Letter> letters : lettersList) {
            String letterJson = "";
            for (Letter letter : letters) {
                if (!letterJson.isEmpty()) letterJson += "||";
                letterJson += gson.toJson(letter);
            }
            lettersChoiceJson.add(letterJson);
        }
        out.putStringArrayList(KEY_LETTERS, lettersChoiceJson);
    }

    @Override
    public RestorableViewState<AssessmentView> restoreInstanceState(Bundle in) {
        counter = in.getInt(KEY_COUNTER, 0);
        // todo: get identification input and selected on multiple choice
        // convert back the JSON String to object list
        Gson gson = new GsonBuilder().create();
        ArrayList<String> userAnswerJson = in.getStringArrayList(KEY_ANSWER);
        ArrayList<String> questionJson = in.getStringArrayList(KEY_ASSESSMENT);
        ArrayList<String> lettersJson = in.getStringArrayList(KEY_LETTERS);

        assessmentList = new ArrayList<>();
        for (String question : questionJson != null ? questionJson : new ArrayList<String>())
            assessmentList.add(gson.fromJson(question, Assessment.class));

        userAnswerList = new ArrayList<>();
        for (String answer : userAnswerJson != null ? userAnswerJson : new ArrayList<String>())
            userAnswerList.add(gson.fromJson(answer, UserAnswer.class));

        lettersList = new ArrayList<>();
        for (String letters : lettersJson != null ? lettersJson : new ArrayList<String>()) {
            String[] lettersArr = letters.split("||");
            List<Letter> letterList = new ArrayList<>();
            for (int i = 0; i < lettersArr.length; i++)
                letterList.add(gson.fromJson(lettersArr[i], Letter.class));
            lettersList.add(letterList);
        }
        return this;
    }

    @Override
    public void apply(AssessmentView view, boolean retained) {
        view.restoreData(counter, assessmentList, userAnswerList, lettersList);
    }

    int getCounter() {
        return counter;
    }

    void setCounter(int counter) {
        this.counter = counter;
    }

    void decrementCounter() {
        counter--;
    }

    void incrementCounter() {
        counter++;
    }

    void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }

    void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
    }

    void setLetterList(List<List<Letter>> lettersList) {
        this.lettersList = lettersList;
    }
}
