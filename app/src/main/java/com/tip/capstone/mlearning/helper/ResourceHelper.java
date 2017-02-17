package com.tip.capstone.mlearning.helper;

import android.content.Context;
import android.util.Log;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Utilities for Images
 */

public class ResourceHelper {

    private static final String TAG = ResourceHelper.class.getSimpleName();

    /**
     * Get the resource id of the drawable using the literal name String.
     *
     * @param context       the context calling the resource
     * @param pVariableName the raw variable name of the resource
     * @return the resource id of the drawable
     */
    public static int getDrawableResourceId(Context context, String pVariableName) {
        try {
            String[] imageName = pVariableName.split("\\.");
            Log.d(TAG, "getDrawableResourceId: imagename " + imageName[0]);
            return context.getResources().getIdentifier(imageName[0], "drawable", context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, "getDrawableResourceId: " + pVariableName, e);
            return -1;
        }
    }

    public static int getRawResourceId(Context context, String pVariableName) {
        try {
            String[] imageName = pVariableName.split("\\.");
            Log.d(TAG, "getRawResourceId: video " + imageName[0]);
            return context.getResources().getIdentifier(imageName[0], "raw", context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, "getRawResourceId: " + pVariableName, e);
            return -1;
        }
    }

}
