package com.tip.capstone.mlearning.ui.simulation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityAcidBasedSimulationBinding;
import com.tip.capstone.mlearning.databinding.DialogAboutBinding;
import com.tip.capstone.mlearning.databinding.DialogComputationBinding;

public class AcidBasedSimulationActivity extends AppCompatActivity {

    ActivityAcidBasedSimulationBinding binding;
    String pick1 = "", pick2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_acid_based_simulation);
        binding.m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPick(Constant.M_STRING);
            }
        });

        binding.p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPick(Constant.P_STRING);
            }
        });

        binding.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPick(Constant.L_STRING);
            }
        });

        binding.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPick(Constant.L_STRING);
            }
        });

        binding.calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pick1.equals("") || pick2.equals("")) {
                    Toast.makeText(AcidBasedSimulationActivity.this, "Input Complete Data!", Toast.LENGTH_SHORT).show();
                } else {
                    calculate(pick1, pick2);
                }
            }
        });

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AcidBasedSimulationActivity.this)
                        .setTitle("Exit?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // todo: compute score and show answer summary
                                dialogInterface.dismiss();
                                AcidBasedSimulationActivity.this.finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAboutBinding dialogBinding = DataBindingUtil.inflate(
                        getLayoutInflater(),
                        R.layout.dialog_about,
                        null,
                        false);
                final Dialog dialog = new Dialog(AcidBasedSimulationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogBinding.getRoot());
                dialogBinding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }

    private void setPick(String pick) {
        if (pick1.equals("")) {
            pick1 = pick;
            binding.pick1.setText(pick);
            binding.phValue.setText("pH = "+this.phValue(pick));
        } else if (pick2.equals("")) {
            pick2 = pick;
            binding.pick2.setText(pick);
            binding.phValue.setText("pH = "+this.phValue(pick));
        } else if (!pick1.equals(pick) || !pick2.equals(pick)) {
            pick1 = pick;
            binding.pick1.setText(pick);
            binding.phValue.setText("pH = "+this.phValue(pick));

            //reset pick 2
            pick2 = "";
            binding.pick2.setText(pick2);
        }
    }

    private void calculate(String pick1, String pick2) {
        DialogComputationBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_computation,
                null,
                false);
        int drawableResult;
        String phResult;
        if ((pick1.equals(Constant.M_STRING) && pick2.equals(Constant.R_STRING)) ||
                (pick2.equals(Constant.M_STRING) && pick1.equals(Constant.R_STRING))) {
            drawableResult =  R.drawable.m_r;
            phResult = "-/log [13.00 x 2.3] = 29.9";
        } else if ((pick1.equals(Constant.M_STRING) && pick2.equals(Constant.P_STRING)) ||
                (pick2.equals(Constant.P_STRING) && pick1.equals(Constant.M_STRING))) {
            drawableResult = R.drawable.m_p;
            phResult = "-/log [13.00 x 1.28] = 16.64";
        } else if ((pick1.equals(Constant.M_STRING) && pick2.equals(Constant.L_STRING)) ||
                (pick2.equals(Constant.L_STRING) && pick1.equals(Constant.M_STRING))) {
            drawableResult = R.drawable.m_l;
            phResult = "-/log [13.00 x 12.55] = 163.15";
        } else if ((pick1.equals(Constant.R_STRING) && pick2.equals(Constant.P_STRING)) ||
                (pick2.equals(Constant.R_STRING) && pick1.equals(Constant.P_STRING))) {
            drawableResult = R.drawable.r_p;
            phResult = "-/log [2.3 x 1.28] = 2.94";
        } else if ((pick1.equals(Constant.R_STRING) && pick2.equals(Constant.L_STRING)) ||
                (pick2.equals(Constant.R_STRING) && pick1.equals(Constant.L_STRING))) {
            drawableResult = R.drawable.r_l;
            phResult = "-/log [2.3 x 12.55] = 28.86";
        } else if ((pick1.equals(Constant.P_STRING) && pick2.equals(Constant.L_STRING)) ||
                (pick2.equals(Constant.P_STRING) && pick1.equals(Constant.L_STRING))) {
            drawableResult = R.drawable.p_l;
            phResult = "-/log [1.28 x 12.55] = 16.06";
        } else {
            Toast.makeText(this, "Mixture failed\nTry others", Toast.LENGTH_SHORT).show();
            phResult = "No Result";
            drawableResult = 0;
        }

        dialogBinding.indicator1.setText(pick1);
        dialogBinding.indicator2.setText(pick2);
        Glide.with(this).load(this.indicatorDawable(pick1)).into(dialogBinding.drawableIndicator1);
        Glide.with(this).load(this.indicatorDawable(pick2)).into(dialogBinding.drawableIndicator2);
        Glide.with(this).load(drawableResult).into(dialogBinding.drawableMixture);
        dialogBinding.phMixture.setText("pH = " + phResult);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private String phValue(String pick) {
        String phValue = "";
        switch (pick) {
            case Constant.M_STRING:
                phValue = "13.00";
                break;
            case Constant.P_STRING:
                phValue = "2.3";
                break;
            case Constant.L_STRING:
                phValue = "1.28";
                break;
            case Constant.R_STRING:
                phValue = "12.55";
                break;
        }

        return phValue;
    }

    private int indicatorDawable(String pick) {
        int indicatorDrawable = 0;
        switch (pick) {
            case Constant.M_STRING:
                indicatorDrawable = R.drawable.m;
                break;
            case Constant.P_STRING:
                indicatorDrawable =  R.drawable.p ;
                break;
            case Constant.L_STRING:
                indicatorDrawable = R.drawable.l;
                break;
            case Constant.R_STRING:
                indicatorDrawable = R.drawable.r;
                break;
        }

        return indicatorDrawable;
    }
}
