package com.tip.capstone.mlearning.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class DateTimeUtils {

    public static final String DATE_ONLY = "MM/dd/yy";

    private static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    private static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toDuration(Date date) {
        try {
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(date); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 8); // adds 8 hour
            // returns new date object, one hour in the future
            Date mDate = cal.getTime();
            Date currentDate = new Date();
            long duration = currentDate.getTime() - mDate.getTime();

            StringBuilder res = new StringBuilder();
            for (int i = 0; i < times.size(); i++) {
                Long current = times.get(i);
                long temp = duration / current;
                if (temp > 0) {
                    res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                    break;
                }
            }
            if ("".equals(res.toString()))
                return "just a moment ago";
            else
                return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to parse data";
        }
    }

    public static String getDateTimeFormatted(long millisSecond) {
        Date date = new Date(millisSecond);
        return new SimpleDateFormat("MM-dd-yy hh:mm a", Locale.US).format(date);
    }

    public static String getLongDateTimeString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date);
    }

    public static Calendar convertTransactionStringDate(String dateString, String format) {
        Date date = convertStringDate(dateString, format);
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date convertStringDate(String dateString, String format) {
        Date date = null;
        try {
            date = getSimpleDateFormat(format).parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("dateString: ", dateString + "");
            e.printStackTrace();
        }
        return date;
    }

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH);
    }

    public static String convertDateToString(String format, Calendar calendar) {
        if (calendar == null) return "";
        return getSimpleDateFormat(format).format(calendar.getTime());
    }

}
