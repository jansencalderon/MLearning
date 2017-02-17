package com.tip.capstone.mlearning.ui.quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemChoiceBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class ChoiceListAdapter extends RecyclerView.Adapter<ChoiceListAdapter.ViewHolder> {

    private static final String TAG = ChoiceListAdapter.class.getSimpleName();
    private final List<Choice> choiceList;
    private boolean[] selected;
    private boolean onBind;

    /**
     * Default Constructor
     */
    public ChoiceListAdapter() {
        choiceList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChoiceBinding itemChoiceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_choice,
                parent,
                false);
        return new ViewHolder(itemChoiceBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Choice choice = choiceList.get(position);
        holder.itemChoiceBinding.setChoice(choice);
        // issues when char increment is one liner
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
     * choice list setup
     *
     * @param choiceList list to display
     */
    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList.clear();
        this.choiceList.addAll(choiceList);
        notifyDataSetChanged();
        resetSelected(-1);
    }

    /**
     * reset checkbox
     *
     * @param adapterPosition position to be checked
     */
    private void resetSelected(int adapterPosition) {
        selected = new boolean[choiceList.size()];
        for (int i = 0; i < choiceList.size(); i++) selected[i] = i == adapterPosition;
        if (!onBind)
            notifyDataSetChanged();
    }

    /**
     * @return selected choice
     */
    public Choice getSelectedChoice() {
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
        private final ItemChoiceBinding itemChoiceBinding;

        ViewHolder(ItemChoiceBinding itemChoiceBinding) {
            super(itemChoiceBinding.getRoot());
            this.itemChoiceBinding = itemChoiceBinding;
            // setup listener as issues if onBindViewHolder
            itemChoiceBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    resetSelected(b ? getAdapterPosition() : -1);
                }
            });
        }
    }
}