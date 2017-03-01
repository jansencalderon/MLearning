package com.tip.capstone.mlearning.ui.tutorial;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tip.capstone.mlearning.model.Tutorial;

import java.util.List;


class TutorialPageAdapter extends FragmentStatePagerAdapter {

    private List<Tutorial> list;
    private Context context;

    public TutorialPageAdapter(FragmentManager fm, Context context, List<Tutorial>list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return TutorialFragment.newInstance(list.get(position).getSequence());
    }
}