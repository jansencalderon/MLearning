package com.tip.capstone.mlearning.ui.lesson.detail;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailImageBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailTextBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonHeaderBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonQuizButtonBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.ui.lesson.LessonView;

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
    private final LessonDetailListView lessonDetailListView;
    private boolean isLastPage;
    private String query;
    private int lessonDetailRef;

    /**
     * Constructor
     *
     * @param lesson          lesson of the list (for header purposes)
     * @param view            for listener of items
     * @param lessonDetailRef
     */
    LessonDetailListAdapter(Lesson lesson, LessonDetailListView view, boolean isLastPage,
                            String query, int lessonDetailRef) {
        this.lesson = lesson;
        this.lessonDetailListView = view;
        this.isLastPage = isLastPage;
        this.query = query;
        this.lessonDetailRef = lessonDetailRef;
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
                final LessonDetail lessonDetail = lessonDetails.get(lesson != null ? position - 1 : position);
                final LessonDetailTextViewHolder lessonDetailTextViewHolder = (LessonDetailTextViewHolder) holder;
                lessonDetailTextViewHolder.itemLessonDetailTextBinding
                        .setLessonDetail(lessonDetail);
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.setView(lessonDetailListView);
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.layoutLessonDetailText
                        .setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                                lessonDetail.getId() == lessonDetailRef ?
                                        R.color.yellow : R.color.white));
                //use a loop to change text color
                String text = lessonDetail.getBody();
                if (query != null && !query.isEmpty() &&
                        text.toUpperCase().contains(query.toUpperCase())) {
                    Log.d(TAG, "onBindViewHolder: id:" + lessonDetail.getId());
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

                lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                lessonDetailListView.onDetailClick(lessonDetail);
                                return true;
                            }
                        });
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody
                        .setOnTouchListener(new View.OnTouchListener() {

                            final float STEP = 200;
                            float mRatio = 1.0f;
                            int mBaseDist;
                            float mBaseRatio;
                            float fontsize = 13;

                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getPointerCount() == 2) {
                                    int action = motionEvent.getAction();
                                    int pureaction = action & MotionEvent.ACTION_MASK;
                                    if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                                        mBaseDist = getDistance(motionEvent);
                                        mBaseRatio = mRatio;
                                    } else {
                                        float delta = (getDistance(motionEvent) - mBaseDist) / STEP;
                                        float multi = (float) Math.pow(2, delta);
                                        mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                                        lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setTextSize(mRatio + 13);
                                    }
                                }
                                return false;
                            }

                            int getDistance(MotionEvent event) {
                                int dx = (int) (event.getX(0) - event.getX(1));
                                int dy = (int) (event.getY(0) - event.getY(1));
                                return (int) (Math.sqrt(dx * dx + dy * dy));
                            }
                        });
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setLongClickable(true);
                break;
            case VIEW_IMAGE:
                LessonDetailImageViewHolder lessonDetailImageViewHolder = (LessonDetailImageViewHolder) holder;
                lessonDetailImageViewHolder.itemLessonDetailImageBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                Log.d(TAG, "onBindViewHolder: " + lessonDetails.get(lesson != null ? position - 1 : position).getBody());

                if (lessonDetails.get(lesson != null ? position - 1 : position).getBody().contains(".gif")) {
                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                    Glide.with(holder.itemView.getContext())
                            .load(ResourceHelper.getRawResourceId(holder.itemView.getContext(), lessonDetails.get(lesson != null ? position - 1 : position)
                                    .getBody()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.ic_photo)
                            .error(R.drawable.ic_photo)
                            .into(imageViewTarget);
                } else {
                    Glide.with(holder.itemView.getContext())
                            .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(),
                                    lessonDetails.get(lesson != null ? position - 1 : position).getBody()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.ic_photo)
                            .error(R.drawable.ic_photo)
                            .into(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                }

                break;
            case VIEW_QUIZ:
                LessonQuizButtonHolder lessonQuizButtonHolder = (LessonQuizButtonHolder) holder;
                lessonQuizButtonHolder.itemLessonQuizButtonBinding.setView(lessonDetailListView);
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