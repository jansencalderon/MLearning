package com.tip.capstone.mlearning.helper;

import android.content.Context;
import android.util.Log;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Utilities for Images
 */

public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    /**
     * Get the resource id of the drawable using the literal name String.
     *
     * @param context       the context calling the resource
     * @param pVariableName the raw variable name of the resource
     * @return the resource id of the drawable
     */
    public static int getResourceId(Context context, String pVariableName) {
        try {
            String[] imageName = pVariableName.split("\\.");
            Log.d(TAG, "getResourceId: imagename " + imageName[0]);
            return context.getResources().getIdentifier(imageName[0], "drawable", context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, "getResourceId: " + pVariableName, e);
            return -1;
        }
    }

}
