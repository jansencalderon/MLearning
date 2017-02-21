package com.tip.capstone.mlearning.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemAssessmentHistoryBinding;
import com.tip.capstone.mlearning.model.AssessmentGrade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 * List Adapter for Grades
 */

public class AssessmentHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AssessmentGrade> gradesList;

    public AssessmentHistoryListAdapter() {
        this.gradesList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAssessmentHistoryBinding itemAssessmentHistoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_assessment_history,
                parent,
                false);
        return  new GradesViewHolder(itemAssessmentHistoryBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GradesViewHolder gradesViewHolder = (GradesViewHolder) holder;
        gradesViewHolder.itemAssessmentHistoryBinding.setGrade(gradesList.get(position));
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    /**
     * @param gradesList list of grades to display
     */
    public void setGradesList(List<AssessmentGrade> gradesList) {
        this.gradesList.clear();
        this.gradesList.addAll(gradesList);
        notifyDataSetChanged();
    }


    private class GradesViewHolder extends RecyclerView.ViewHolder {

        private final ItemAssessmentHistoryBinding itemAssessmentHistoryBinding;

        GradesViewHolder(ItemAssessmentHistoryBinding itemAssessmentHistoryBinding) {
            super(itemAssessmentHistoryBinding.getRoot());
            this.itemAssessmentHistoryBinding = itemAssessmentHistoryBinding;
        }
    }

}
