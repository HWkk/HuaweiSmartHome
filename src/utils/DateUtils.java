package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
}
