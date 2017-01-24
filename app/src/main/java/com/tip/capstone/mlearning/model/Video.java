package com.tip.capstone.mlearning.model;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class Video {

    private int resourceId;
    private String rawName;
    private String title;

    public Video(int resourceId, String rawName, String title) {
        this.resourceId = resourceId;
        this.rawName = rawName;
        this.title = title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
