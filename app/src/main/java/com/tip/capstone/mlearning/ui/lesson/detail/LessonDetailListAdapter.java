package com.tip.capstone.mlearning.ui.lesson.detail;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailImageBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailTextBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonHeaderBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonQuizButtonBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Adapter for Lesson Details
 */

class LessonDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_TEXT = 1;
    private static final int VIEW_IMAGE = 2;
    private static final int VIEW_QUIZ = 3;
    private static final String TAG = LessonDetailListAdapter.class.getSimpleName();

    private final Lesson lesson;
    private final List<LessonDetail> lessonDetails;
    private final LessonDetailListView view;
    private boolean isLastPage;
    private String query;

    /**
     * Constructor
     *
     * @param lesson lesson of the list (for header purposes)
     * @param view   for listener of items
     */
    LessonDetailListAdapter(Lesson lesson, LessonDetailListView view, boolean isLastPage, String query) {
        this.lesson = lesson;
        this.view = view;
        this.isLastPage = isLastPage;
        this.query = query;
        lessonDetails = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && lesson != null) {
            return VIEW_HEADER;
        } else if (isLastPage && position == (getItemCount() - 1)) {
            return VIEW_QUIZ;
        } else {
            // decrement index for list if has lesson
            int index = lesson != null ? position - 1 : position;
            switch (lessonDetails.get(index).getBody_type()) {
                case Constant.DETAIL_TYPE_TEXT:
                    return VIEW_TEXT;
                case Constant.DETAIL_TYPE_IMAGE:
                    return VIEW_IMAGE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                ItemLessonHeaderBinding itemLessonHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_header,
                        parent,
                        false);
                return new LessonHeaderViewHolder(itemLessonHeaderBinding);
            case VIEW_TEXT:
                ItemLessonDetailTextBinding itemLessonDetailTextBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_text,
                        parent,
                        false);
                return new LessonDetailTextViewHolder(itemLessonDetailTextBinding);
            case VIEW_IMAGE:
                ItemLessonDetailImageBinding itemLessonDetailImageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_image,
                        parent,
                        false);
                return new LessonDetailImageViewHolder(itemLessonDetailImageBinding);
            case VIEW_QUIZ:
                ItemLessonQuizButtonBinding itemLessonQuizButtonBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_quiz_button,
                        parent,
                        false);
                return new LessonQuizButtonHolder(itemLessonQuizButtonBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HEADER:
                LessonHeaderViewHolder lessonHeaderViewHolder = (LessonHeaderViewHolder) holder;
                lessonHeaderViewHolder.itemLessonHeaderBinding.setLesson(lesson);
                break;
            case VIEW_TEXT:
                LessonDetailTextViewHolder lessonDetailTextViewHolder = (LessonDetailTextViewHolder) holder;
                lessonDetailTextViewHolder.itemLessonDetailTextBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.setView(view);

                //use a loop to change text color
                String text = lessonDetailTextViewHolder.itemLessonDetailTextBinding.getLessonDetail().getBody();
                if (query != null && !query.isEmpty() &&
                        text.toUpperCase().contains(query.toUpperCase())) {
                    Log.d(TAG, "onBindViewHolder: id:" + lessonDetailTextViewHolder.itemLessonDetailTextBinding.getLessonDetail().getId());
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                    Spannable WordtoSpan = new SpannableString(text);
                    int startIndex = text.toUpperCase().indexOf(query.toUpperCase());
                    int stopIndex = startIndex + query.length();
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    WordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setText(WordtoSpan);
                } else {
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                }
                break;
            case VIEW_IMAGE:
                LessonDetailImageViewHolder lessonDetailImageViewHolder = (LessonDetailImageViewHolder) holder;
                lessonDetailImageViewHolder.itemLessonDetailImageBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                Log.d(TAG, "onBindViewHolder: " + lessonDetails.get(lesson != null ? position - 1 : position).getBody());

                Glide.with(holder.itemView.getContext())
                        .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(),
                                lessonDetails.get(lesson != null ? position - 1 : position).getBody()))
                        .into(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                break;
            case VIEW_QUIZ:
                LessonQuizButtonHolder lessonQuizButtonHolder = (LessonQuizButtonHolder) holder;
                lessonQuizButtonHolder.itemLessonQuizButtonBinding.setView(view);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = lessonDetails.size();
        if (lesson != null) size++;
        if (isLastPage) size++;
        return size;
    }

    /**
     * @param lessonDetails list of lesson details to display
     */
    void setLessonDetails(List<LessonDetail> lessonDetails) {
        this.lessonDetails.clear();
        this.lessonDetails.addAll(lessonDetails);
        notifyDataSetChanged();
    }

    private class LessonHeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonHeaderBinding itemLessonHeaderBinding;

        LessonHeaderViewHolder(ItemLessonHeaderBinding itemLessonHeaderBinding) {
            super(itemLessonHeaderBinding.getRoot());
            this.itemLessonHeaderBinding = itemLessonHeaderBinding;
        }
    }

    private class LessonDetailTextViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailTextBinding itemLessonDetailTextBinding;

        LessonDetailTextViewHolder(ItemLessonDetailTextBinding itemLessonDetailTextBinding) {
            super(itemLessonDetailTextBinding.getRoot());
            this.itemLessonDetailTextBinding = itemLessonDetailTextBinding;
        }
    }

    private class LessonDetailImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailImageBinding itemLessonDetailImageBinding;

        LessonDetailImageViewHolder(ItemLessonDetailImageBinding itemLessonDetailImageBinding) {
            super(itemLessonDetailImageBinding.getRoot());
            this.itemLessonDetailImageBinding = itemLessonDetailImageBinding;
        }
    }

    private class LessonQuizButtonHolder extends RecyclerView.ViewHolder {
        private final ItemLessonQuizButtonBinding itemLessonQuizButtonBinding;

        LessonQuizButtonHolder(ItemLessonQuizButtonBinding itemLessonQuizButtonBinding) {
            super(itemLessonQuizButtonBinding.getRoot());
            this.itemLessonQuizButtonBinding = itemLessonQuizButtonBinding;
        }
    }

}