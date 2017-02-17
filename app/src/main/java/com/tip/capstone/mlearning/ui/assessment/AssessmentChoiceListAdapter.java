package com.tip.capstone.mlearning.ui.assessment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemAssessmentChoiceBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.AssessmentChoice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 * Adapter for Assessment Multiple Choice
 */

class AssessmentChoiceListAdapter extends RecyclerView.Adapter<AssessmentChoiceListAdapter.ViewHolder> {

    private static final String TAG = AssessmentChoiceListAdapter.class.getSimpleName();
    private final List<AssessmentChoice> choiceList;
    private boolean[] selected;
    private boolean onBind;

    AssessmentChoiceListAdapter() {
        choiceList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAssessmentChoiceBinding itemChoiceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_assessment_choice,
                parent,
                false);
        return new ViewHolder(itemChoiceBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AssessmentChoice choice = choiceList.get(position);
        holder.itemChoiceBinding.setChoice(choice);
        // having error on making char increment one liner
        char x = 'A';
        x += position;
        holder.itemChoiceBinding.setLetter(x + "");
        if (choice.getChoice_type() == Constant.DETAIL_TYPE_IMAGE) {
            Glide.with(holder.itemView.getContext())
                    .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(), choice.getBody()))
                    .into(holder.itemChoiceBinding.imgChoice);
        }
        onBind = true;
        holder.itemChoiceBinding.checkbox.setChecked(selected[position]);
        onBind = false;
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    /**
     * Setup choices
     *
     * @param choiceList list of choice to display
     */
    void setChoiceList(List<AssessmentChoice> choiceList) {
        this.choiceList.clear();
        this.choiceList.addAll(choiceList);
        notifyDataSetChanged();
        resetSelected(-1);
    }

    /**
     * Reset the checkbox to except the pass param
     *
     * @param adapterPosition the position of choice checked
     */
    private void resetSelected(int adapterPosition) {
        Log.d(TAG, "resetSelected: " + adapterPosition);
        selected = new boolean[choiceList.size()];
        for (int i = 0; i < choiceList.size(); i++) {
            selected[i] = i == adapterPosition;
        }
        if (!onBind) notifyDataSetChanged();
    }

    /**
     * @return Selected Choice
     */
    AssessmentChoice getSelectedChoice() {
        for (int i = 0; i < selected.length; i++) if (selected[i]) return choiceList.get(i);
        return null;
    }

    public void setAnswer(String userAnswer) {
        Log.d(TAG, "setAnswer: user: " + userAnswer);
        for (int i = 0; i < choiceList.size(); i++)
            if (choiceList.get(i).getBody().contentEquals(userAnswer)) {
                Log.d(TAG, "setAnswer: " + i + ", " + choiceList.get(i).getBody());
                resetSelected(i);
            }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAssessmentChoiceBinding itemChoiceBinding;

        ViewHolder(ItemAssessmentChoiceBinding itemChoiceBinding) {
            super(itemChoiceBinding.getRoot());
            this.itemChoiceBinding = itemChoiceBinding;
            // setup listener here as it arises issues if in onBindViewHolder()
            itemChoiceBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) resetSelected(getAdapterPosition());
                }
            });
        }
    }
}
