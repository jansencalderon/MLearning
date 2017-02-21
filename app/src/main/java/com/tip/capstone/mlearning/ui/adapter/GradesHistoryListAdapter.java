package com.tip.capstone.mlearning.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemQuizHistoryBinding;
import com.tip.capstone.mlearning.model.QuizGrade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 * List Adapter for Grades
 */

public class GradesHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<QuizGrade> gradesList;

    public GradesHistoryListAdapter() {
        this.gradesList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemQuizHistoryBinding itemQuizHistoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_quiz_history,
                parent,
                false);
        return  new GradesViewHolder(itemQuizHistoryBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GradesViewHolder gradesViewHolder = (GradesViewHolder) holder;
        gradesViewHolder.itemQuizHistoryBinding.setGrade(gradesList.get(position));
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    /**
     * @param gradesList list of grades to display
     */
    public void setGradesList(List<QuizGrade> gradesList) {
        this.gradesList.clear();
        this.gradesList.addAll(gradesList);
        notifyDataSetChanged();
    }


    private class GradesViewHolder extends RecyclerView.ViewHolder {

        private final ItemQuizHistoryBinding itemQuizHistoryBinding;

        GradesViewHolder(ItemQuizHistoryBinding itemQuizHistoryBinding) {
            super(itemQuizHistoryBinding.getRoot());
            this.itemQuizHistoryBinding = itemQuizHistoryBinding;
        }
    }

}
