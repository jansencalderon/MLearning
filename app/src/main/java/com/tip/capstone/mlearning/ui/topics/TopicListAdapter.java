package com.tip.capstone.mlearning.ui.topics;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemTopicBinding;
import com.tip.capstone.mlearning.helper.ImageHelper;
import com.tip.capstone.mlearning.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private final List<Topic> topicList;
    private final Context context;
    private final TopicListView topicListView;

    public TopicListAdapter(Context context, TopicListView topicListView) {
        this.context = context;
        this.topicListView = topicListView;
        topicList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTopicBinding itemTopicBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_topic,
                parent,
                false
        );
        return new ViewHolder(itemTopicBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemTopicBinding.setView(topicListView);
        holder.itemTopicBinding.setTopic(topicList.get(position));
        Glide.with(context)
                .load(ImageHelper.getResourceId(context, topicList.get(position).getImage()))
                .centerCrop()
                .into(holder.itemTopicBinding.imageTopic);
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList.clear();
        this.topicList.addAll(topicList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTopicBinding itemTopicBinding;

        public ViewHolder(ItemTopicBinding itemTopicBinding) {
            super(itemTopicBinding.getRoot());
            this.itemTopicBinding = itemTopicBinding;
        }
    }
}
