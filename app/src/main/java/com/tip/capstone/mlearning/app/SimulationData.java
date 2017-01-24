package com.tip.capstone.mlearning.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tip.capstone.mlearning.R;

/**
 * @author pocholomia
 * @since 01/12/2016
 */

public class SimulationData {

    public static final int[] SIMULATION_DRAWABLES = {
            R.drawable.circle_red,
            R.drawable.circle_blue,
            R.drawable.circle_yellow};

    public static final int[][] DRAWABLE_PAIRS = { // third is the combination of the first 2 drawables
            {R.drawable.circle_red, R.drawable.circle_blue, R.drawable.circle_purple},
            {R.drawable.circle_red, R.drawable.circle_yellow, R.drawable.circle_orange},
            {R.drawable.circle_blue, R.drawable.circle_yellow, R.drawable.circle_green},
    };

    private static final String TAG = SimulationData.class.getSimpleName();

    public static Drawable getResultDrawable(Context context, Drawable drawableA, Drawable drawableB) {
        for (int x = 0; x < DRAWABLE_PAIRS.length; x++) {
            Log.d(TAG, "getResultDrawable: " + DRAWABLE_PAIRS[x]);
            if ((drawableA.getConstantState() == ContextCompat.getDrawable(context, DRAWABLE_PAIRS[x][0]).getConstantState()
                    && drawableB.getConstantState() == ContextCompat.getDrawable(context, DRAWABLE_PAIRS[x][1]).getConstantState())
                    ||
                    (drawableA.getConstantState() == ContextCompat.getDrawable(context, DRAWABLE_PAIRS[x][1]).getConstantState()
                            && drawableB.getConstantState() == ContextCompat.getDrawable(context, DRAWABLE_PAIRS[x][0]).getConstantState())) {
                return ContextCompat.getDrawable(context, DRAWABLE_PAIRS[x][2]);
            }
        }
        return null;
    }

}
