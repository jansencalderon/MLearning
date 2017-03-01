package com.tip.capstone.mlearning.model;

import android.graphics.drawable.Drawable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sen on 2/26/2017.
 */

public class Tutorial extends RealmObject{

    @PrimaryKey
    private int sequence;
    private String string;
    private String drawable;


    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }
}
