package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 07/12/2016
 */

public class PreQuizGrade extends RealmObject {

    @PrimaryKey
    private int id;
    private int rawScore;
    private int itemCount;
    private long dateUpdated;
    private float floatId;
    private float floatRawScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public float getFloatId() {
        return (float) getId();
    }

    public float getFloatRawScore() {
        return (float) getRawScore();
    }

    /**
     * @return average of the grade score/items * 50 + 50
     */
    public long average() {
        return Math.round(((double) rawScore / (double) itemCount) * 50.0) + 50;
    }
}
