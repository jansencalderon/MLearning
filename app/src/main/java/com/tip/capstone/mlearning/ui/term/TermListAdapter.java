package com.tip.capstone.mlearning.ui.term;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemTermBinding;
import com.tip.capstone.mlearning.model.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ViewHolder> {

    private final List<Term> termList;
    private final TermView termView;

    public TermListAdapter(TermView termView) {
        this.termView = termView;
        termList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTermBinding itemTermBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_term,
                parent,
                false);
        return new ViewHolder(itemTermBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemTermBinding.setTerm(termList.get(position));
        holder.itemTermBinding.setView(termView);
    }

    public void setTermList(List<Term> termList) {
        this.termList.clear();
        this.termList.addAll(termList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTermBinding itemTermBinding;

        public ViewHolder(ItemTermBinding itemTermBinding) {
            super(itemTermBinding.getRoot());
            this.itemTermBinding = itemTermBinding;
        }
    }
}
