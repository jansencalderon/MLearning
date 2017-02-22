package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;

/**
 * Created by Cholo Mia on 2/22/2017.
 */

public class Note extends RealmObject {

    private int topicId;
    private String content;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
