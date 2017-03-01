package com.tip.capstone.mlearning.ui.tutorial;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityTutorialBinding;
import com.tip.capstone.mlearning.model.Tutorial;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class TutorialActivity extends AppCompatActivity {

    Realm realm;
    ActivityTutorialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");
        List<Tutorial> tutorials = realm.where(Tutorial.class).findAllSorted("sequence",Sort.ASCENDING);
        TutorialPageAdapter tutorialPageAdapter = new TutorialPageAdapter(getSupportFragmentManager(), this, tutorials);
        binding.viewPager.setAdapter(tutorialPageAdapter);
       /* for (int i = 0; i > tutorials.size(); i++) {

        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
