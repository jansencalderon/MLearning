package com.tip.capstone.mlearning.ui.glossary;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemGlossaryBinding;
import com.tip.capstone.mlearning.model.Glossary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 02/12/2016
 */

public class GlossaryListAdapter extends RecyclerView.Adapter<GlossaryListAdapter.ViewHolder> {

    private List<Glossary> glossaryList;

    public GlossaryListAdapter() {
        glossaryList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGlossaryBinding itemGlossaryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_glossary,
                parent,
                false);
        return new ViewHolder(itemGlossaryBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemGlossaryBinding.setGlossary(glossaryList.get(position));
    }

    @Override
    public int getItemCount() {
        return glossaryList.size();
    }

    public void setGlossaryList(List<Glossary> glossaryList) {
        this.glossaryList.clear();
        this.glossaryList.addAll(glossaryList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGlossaryBinding itemGlossaryBinding;

        public ViewHolder(ItemGlossaryBinding itemGlossaryBinding) {
            super(itemGlossaryBinding.getRoot());
            this.itemGlossaryBinding = itemGlossaryBinding;
        }
    }
}
