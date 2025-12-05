package com.scsa.memo;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.util.Date;
import java.util.Locale;

public class Util {

    public static String getFormattedDate(long time) {
        // formatting
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss E",
                Locale.KOREA);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return format.format(time);
    }
    // TODO: formatted -> time
}
