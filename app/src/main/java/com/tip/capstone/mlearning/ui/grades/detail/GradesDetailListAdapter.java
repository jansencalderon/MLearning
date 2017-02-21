package com.tip.capstone.mlearning.ui.grades.detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemGradeAssessmentBinding;
import com.tip.capstone.mlearning.databinding.ItemGradesBinding;
import com.tip.capstone.mlearning.databinding.ItemGradesHeaderBinding;
import com.tip.capstone.mlearning.model.Grades;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 * List Adapter for Grades
 */

class GradesDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_ASSESSMENT = 1;
    private static final int VIEW_GRADE = 2;
    private final List<Grades> gradesList;
    private GradesDetailView gradesDetailView;

    GradesDetailListAdapter(GradesDetailView gradesDetailView) {
        this.gradesList = new ArrayList<>();
        this.gradesDetailView = gradesDetailView;
    }

    @Override
    public int getItemViewType(int position) {
        if (gradesList.get(position).isHeader()) {
            return VIEW_HEADER;
        } else if (gradesList.get(position).getAssessmentGrade() != null) {
            return VIEW_ASSESSMENT;
        } else {
            return VIEW_GRADE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                ItemGradesHeaderBinding itemGradesHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_grades_header,
                        parent,
                        false);
                return new HeaderViewHolder(itemGradesHeaderBinding);
            case VIEW_ASSESSMENT:
                ItemGradeAssessmentBinding itemGradeAssessmentBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_grade_assessment,
                        parent,
                        false);
                return new AssessmentViewHolder(itemGradeAssessmentBinding);
            case VIEW_GRADE:
                ItemGradesBinding itemGradesBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_grades,
                        parent,
                        false);
                return new GradesViewHolder(itemGradesBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.itemGradesHeaderBinding.setGrade(gradesList.get(position));
                headerViewHolder.itemGradesHeaderBinding.setView(gradesDetailView);
                break;
            case VIEW_ASSESSMENT:
                AssessmentViewHolder assessmentViewHolder = (AssessmentViewHolder) holder;
                assessmentViewHolder.itemGradeAssessmentBinding.setGrade(gradesList.get(position));
                assessmentViewHolder.itemGradeAssessmentBinding.setView(gradesDetailView);
                break;
            case VIEW_GRADE:
                GradesViewHolder gradesViewHolder = (GradesViewHolder) holder;
                gradesViewHolder.itemGradesBinding.setGrade(gradesList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    /**
     * @param gradesList list of grades to display
     */
    void setGradesList(List<Grades> gradesList) {
        this.gradesList.clear();
        this.gradesList.addAll(gradesList);
        notifyDataSetChanged();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemGradesHeaderBinding itemGradesHeaderBinding;

        HeaderViewHolder(ItemGradesHeaderBinding itemGradesHeaderBinding) {
            super(itemGradesHeaderBinding.getRoot());
            this.itemGradesHeaderBinding = itemGradesHeaderBinding;
        }
    }

    private class AssessmentViewHolder extends RecyclerView.ViewHolder {

        private final ItemGradeAssessmentBinding itemGradeAssessmentBinding;

        AssessmentViewHolder(ItemGradeAssessmentBinding itemGradeAssessmentBinding) {
            super(itemGradeAssessmentBinding.getRoot());
            this.itemGradeAssessmentBinding = itemGradeAssessmentBinding;
        }
    }

    private class GradesViewHolder extends RecyclerView.ViewHolder {

        private final ItemGradesBinding itemGradesBinding;

        GradesViewHolder(ItemGradesBinding itemGradesBinding) {
            super(itemGradesBinding.getRoot());
            this.itemGradesBinding = itemGradesBinding;
        }
    }

}
