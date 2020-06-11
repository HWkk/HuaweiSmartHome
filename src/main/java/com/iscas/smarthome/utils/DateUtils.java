package com.iscas.smarthome.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理类
 */
public class DateUtils {

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static int twoTimeDiff(String t1, String t2) {
        return Math.abs(toSecond(t1) - toSecond(t2));
    }

    public static int toSecond(String time) {
        String[] t = time.split(":");
        return Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
    }
}
