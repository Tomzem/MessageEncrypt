package com.example.mao.util;

import com.example.mao.app.New_Application;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tomze
 * @time 2019年04月20日 20:30
 * @desc
 */
public class TimeUtil {

    public static final String FORMAT_DD = "dd";
    public static boolean isNewDay() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DD);
        String dd = df.format(new Date());
        String ddCache = New_SharePre.getData("dd", dd);
        if (!dd.equals(ddCache)) {
            New_SharePre.saveData("dd", dd);
            return true;
        }
        return false;
    }
}
