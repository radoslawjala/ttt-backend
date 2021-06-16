package es.com.util;

import java.text.SimpleDateFormat;

public class StringUtils {

    private static final String TIME_FORMATTER= "HH:mm:ss";

    public static String getCurrentFormattedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMATTER);
        return simpleDateFormat.format(time);
    }
}
