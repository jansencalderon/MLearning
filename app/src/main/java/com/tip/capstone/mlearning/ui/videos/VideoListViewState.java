package com.tip.capstone.mlearning.ui.videos;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class VideoListViewState implements RestorableViewState<VideoListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<VideoListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(VideoListView view, boolean retained) {

    }
}
