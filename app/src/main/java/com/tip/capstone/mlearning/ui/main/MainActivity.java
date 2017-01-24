package com.tip.capstone.mlearning.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivityMainBinding;
import com.tip.capstone.mlearning.ui.assessment.AssessmentActivity;
import com.tip.capstone.mlearning.ui.glossary.GlossaryActivity;
import com.tip.capstone.mlearning.ui.grades.GradesActivity;
import com.tip.capstone.mlearning.ui.simulation.ReviseSimulationActivity;
import com.tip.capstone.mlearning.ui.simulation.SimulationActivity;
import com.tip.capstone.mlearning.ui.term.TermActivity;
import com.tip.capstone.mlearning.ui.videos.VideoListActivity;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(getMvpView());
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onStudyClicked() {
        startActivity(new Intent(this, TermActivity.class));
    }

    @Override
    public void onAssessmentClicked() {
        startActivity(new Intent(this, AssessmentActivity.class));
    }

    @Override
    public void onGradesClicked() {
        startActivity(new Intent(this, GradesActivity.class));
    }

    @Override
    public void onVideosClicked() {
        startActivity(new Intent(this, VideoListActivity.class));
    }

    @Override
    public void onSimulationClicked() {
        startActivity(new Intent(this, ReviseSimulationActivity.class));
    }

    @Override
    public void onGlossaryClicked() {
        startActivity(new Intent(this, GlossaryActivity.class));
    }
}
