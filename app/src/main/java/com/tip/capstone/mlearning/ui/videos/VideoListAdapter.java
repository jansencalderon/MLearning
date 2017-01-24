package com.tip.capstone.mlearning.ui.videos;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemVideoBinding;
import com.tip.capstone.mlearning.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final String TAG = VideoListAdapter.class.getSimpleName();
    private final List<Video> videoList;
    private VideoListView videoListView;

    public VideoListAdapter(VideoListView videoListView) {
        videoList = new ArrayList<>();
        this.videoListView = videoListView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVideoBinding itemVideoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_video,
                parent,
                false);
        return new ViewHolder(itemVideoBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri videoURI = Uri.parse("android.resource://"
                + holder.itemView.getContext().getPackageName()
                + "/raw/" + videoList.get(position).getRawName());
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(holder.itemView.getContext().getApplicationContext(), videoURI);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        holder.itemVideoBinding.image.setImageBitmap(thumb);
        holder.itemVideoBinding.setVideo(videoList.get(position));
        holder.itemVideoBinding.setView(videoListView);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList.clear();
        this.videoList.addAll(videoList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoBinding itemVideoBinding;

        public ViewHolder(ItemVideoBinding itemVideoBinding) {
            super(itemVideoBinding.getRoot());
            this.itemVideoBinding = itemVideoBinding;
        }
    }
}