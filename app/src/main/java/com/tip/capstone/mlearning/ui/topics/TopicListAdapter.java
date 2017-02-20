package com.tip.capstone.mlearning.ui.topics;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemTermAssessmentBinding;
import com.tip.capstone.mlearning.databinding.ItemTopicBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ASSESSMENT = 1;
    private static final int VIEW_TOPIC = 2;
    private final List<Topic> topicList;
    private final Context context;
    private final TopicListView topicListView;
    private boolean isAssessmentOkayToTake;

    public TopicListAdapter(Context context, TopicListView topicListView) {
        this.context = context;
        this.topicListView = topicListView;
        topicList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == topicList.size()) {
            return VIEW_ASSESSMENT;
        } else {
            return VIEW_TOPIC;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ASSESSMENT) {
            ItemTermAssessmentBinding itemTermAssessmentBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_term_assessment,
                    parent, false
            );
            return new AssessmentViewHolder(itemTermAssessmentBinding);
        } else {
            ItemTopicBinding itemTopicBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_topic,
                    parent,
                    false
            );
            return new TopicViewHolder(itemTopicBinding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TOPIC) {
            TopicViewHolder topicViewHolder = (TopicViewHolder) holder;
            topicViewHolder.itemTopicBinding.setView(topicListView);
            topicViewHolder.itemTopicBinding.setTopic(topicList.get(position));
            Glide.with(context)
                    .load(ResourceHelper.getDrawableResourceId(context, topicList.get(position).getImage()))
                    .centerCrop()
                    .into(topicViewHolder.itemTopicBinding.imageTopic);
        } else {
            AssessmentViewHolder assessmentViewHolder = (AssessmentViewHolder) holder;
            assessmentViewHolder.itemTermAssessmentBinding.setView(topicListView);
            assessmentViewHolder.itemTermAssessmentBinding.setEnable(isAssessmentOkayToTake);
        }

    }

    @Override
    public int getItemCount() {
        return topicList.size() + 1;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList.clear();
        this.topicList.addAll(topicList);
        notifyDataSetChanged();
    }

    public void setAssessmentEnable(boolean isAssessmentOkayToTake) {
        this.isAssessmentOkayToTake = isAssessmentOkayToTake;
        notifyDataSetChanged();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private final ItemTopicBinding itemTopicBinding;

        public TopicViewHolder(ItemTopicBinding itemTopicBinding) {
            super(itemTopicBinding.getRoot());
            this.itemTopicBinding = itemTopicBinding;
        }
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final ItemTermAssessmentBinding itemTermAssessmentBinding;

        public AssessmentViewHolder(ItemTermAssessmentBinding itemTermAssessmentBinding) {
            super(itemTermAssessmentBinding.getRoot());
            this.itemTermAssessmentBinding = itemTermAssessmentBinding;
        }
    }
}
