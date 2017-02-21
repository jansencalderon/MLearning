package com.tip.capstone.mlearning.ui.grades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityGradesBinding;
import com.tip.capstone.mlearning.ui.grades.detail.GradesDetailActivity;

@SuppressLint("ConstantCondition")
public class GradesActivity extends MvpActivity<GradesView, GradesPresenter> implements GradesView {
    ActivityGradesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_grades);

        // assumes that theme has toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Assessment Results");

        binding.setView(getMvpView());


    }

    @NonNull
    @Override
    public GradesPresenter createPresenter() {
        return new GradesPresenter();
    }

    @Override
    public void Prelim(){
        Intent i = new Intent(this, GradesDetailActivity.class);
        i.putExtra(Constant.ID,1);
        startActivity(i);
    }

    @Override
    public void Midterm(){
        Intent i = new Intent(this, GradesDetailActivity.class);
        i.putExtra(Constant.ID,2);
        startActivity(i);
    }

    @Override
    public void Finals(){
        Intent i = new Intent(this, GradesDetailActivity.class);
        i.putExtra(Constant.ID,3);
        startActivity(i);
    }

    @Override
    public void All(){
        Intent i = new Intent(this, GradesDetailActivity.class);
        i.putExtra(Constant.ID,-1);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

