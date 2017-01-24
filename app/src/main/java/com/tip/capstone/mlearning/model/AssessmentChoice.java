package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 24/11/2016
 */

public class AssessmentChoice extends RealmObject {

    @PrimaryKey
    private int id;
    private int questionId;
    private int choice_type;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getChoice_type() {
        return choice_type;
    }

    public void setChoice_type(int choice_type) {
        this.choice_type = choice_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
