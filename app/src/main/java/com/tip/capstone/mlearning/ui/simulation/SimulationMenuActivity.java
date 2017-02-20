package com.tip.capstone.mlearning.ui.simulation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivitySimulationMenuBinding;

public class SimulationMenuActivity extends AppCompatActivity {

    ActivitySimulationMenuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simulation_menu);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: 24/11/2016 setup activity title on manifest instead of here
        getSupportActionBar().setTitle("Simulations");

        binding.acidBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SimulationMenuActivity.this, AcidBasedSimulationActivity.class));
            }
        });

        binding.volatileOils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SimulationMenuActivity.this, ReviseSimulationActivity.class));
            }
        });
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
