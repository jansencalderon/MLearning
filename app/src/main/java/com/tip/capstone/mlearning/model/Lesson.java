package com.tip.capstone.mlearning.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class Lesson extends RealmObject {

    public static final String COL_SEQ = "sequence";
    @PrimaryKey
    private int id;
    private int sequence;
    private String title;
    private String description;
    private RealmList<LessonDetail> lessondetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<LessonDetail> getLessondetails() {
        return lessondetails;
    }

    public void setLessondetails(RealmList<LessonDetail> lessondetails) {
        this.lessondetails = lessondetails;
    }
}
