package com.example.mao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tomze
 * @time 2019年04月12日 0:29
 * @desc
 */
public class New_TimeUtil {

    public static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_YM = "yyyy-MM";
    public static final String FORMAT_YY = "yyyy";
    public static final String FORMAT_MM = "MM";
    public static final String FORMAT_DD = "dd";

    public static final String FORMAT_HM = "HH:mm";
    public static final String FORMAT_HH = "HH";
    public static final String FORMAT_SM = "mm";
    public static final String FORMAT_EE = "EEEE";

    /**
     * 格式化时间
     *
     * @param format
     * @return
     */
    public static String parse(String format) {
        return parse(null, format);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String parse(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        if (date == null) {
            return df.format(new Date());
        } else {
            return df.format(date);
        }
    }

    public static boolean isRunYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

}
