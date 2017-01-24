package com.tip.capstone.mlearning.ui.videos;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Video;

import java.util.List;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public interface VideoListView extends MvpView {

    void onVideoClicked(Video video);

    void setVideoList(List<Video> videoList);
}
