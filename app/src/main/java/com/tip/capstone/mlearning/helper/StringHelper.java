package com.tip.capstone.mlearning.helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Utilities ofr String
 */

public class StringHelper {

    /**
     * Get the String inside the raw file.
     *
     * @param ctx   the context calling this function/method
     * @param resId the resource id of the raw file
     * @return the String inside the raw file
     */
    public static String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
