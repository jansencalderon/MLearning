package com.tip.capstone.mlearning.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class Question extends RealmObject {

    @PrimaryKey
    private int id;
    private String question;
    private String answer;
    private int question_type;
    private RealmList<Choice> choices;
    private int lesson_detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public RealmList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(RealmList<Choice> choices) {
        this.choices = choices;
    }

    public int getLesson_detail() {
        return lesson_detail;
    }

    public void setLesson_detail(int lesson_detail) {
        this.lesson_detail = lesson_detail;
    }
}
