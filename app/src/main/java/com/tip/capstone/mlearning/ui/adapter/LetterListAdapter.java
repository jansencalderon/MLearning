package com.tip.capstone.mlearning.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemLetterBinding;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.ui.views.IdentificationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 08/12/2016
 */

public class LetterListAdapter extends RecyclerView.Adapter<LetterListAdapter.ViewHolder> {

    private static final String TAG = LetterListAdapter.class.getSimpleName();
    private List<Letter> letters;
    private IdentificationView identificationView;
    private boolean choice;

    public LetterListAdapter(IdentificationView identificationView, boolean choice) {
        this.identificationView = identificationView;
        this.choice = choice;
        letters = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLetterBinding itemLetterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_letter, parent, false);
        return new ViewHolder(itemLetterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemLetterBinding.setPosition(position);
        holder.itemLetterBinding.setChoice(choice);
        holder.itemLetterBinding.setLetter(letters.get(position));


        if (letters.get(position).getLetter().isEmpty() || letters.get(position).isSpace()) {
            holder.itemLetterBinding.setView(null);
        } else {
            holder.itemLetterBinding.setView(identificationView);
        }

    }

    @Override
    public int getItemCount() {
        return letters.size();
    }

    public void setLetters(List<Letter> letters) {
        this.letters.clear();
        this.letters.addAll(letters);
        notifyDataSetChanged();
    }

    public void removeLetter(int position) {
        Letter letter1 = letters.get(position);
        letter1.setLetter("");
        letters.set(position, letter1);
        notifyItemChanged(position);
    }

    public void addLetter(String letter, int emptyIndex) {
        Log.d(TAG, "addLetter: letter " + letter);
        Letter letter1 = letters.get(emptyIndex);
        letter1.setLetter(letter);
        letters.set(emptyIndex, letter1);
        letters.get(emptyIndex).getLetter();
        Log.d(TAG, "addLetter: after letter " + letters.get(emptyIndex).getLetter());
        notifyItemChanged(emptyIndex);
    }

    public String getAnswer() {
        String answer = "";
        for (Letter letter : letters) answer += letter.getLetter();
        return answer;
    }

    public boolean completeAnswer() {
        return getAnswer().length() == letters.size();
    }

    public int getEmptyIndex() {
        for (int i = 0; i < letters.size(); i++)
            if (!letters.get(i).isSpace() && letters.get(i).getLetter().isEmpty()) return i;
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLetterBinding itemLetterBinding;

        public ViewHolder(ItemLetterBinding itemLetterBinding) {
            super(itemLetterBinding.getRoot());
            this.itemLetterBinding = itemLetterBinding;
        }
    }
}
