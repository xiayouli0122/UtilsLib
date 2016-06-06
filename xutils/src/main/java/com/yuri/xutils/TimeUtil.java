package com.yuri.xutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关常用工具类
 */
public class TimeUtil {

    /**
     * 获取当前时间，并格式化 format "yyyy-MM-dd_HH"
     * @param template like "yyyy-MM-dd_HH:mm:ss",or "yyyy-MM-dd",or "yyyy年MM月dd日"
     * @return 当前时间格式化后的字符
     */
    public static String getDate(String template) {
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
    * 获取当前时间，并格式化
    * @param template like "yyyy-MM-dd_HH:mm:ss",or "yyyy-MM-dd",or "yyyy年MM月dd日"
            * @param time time in mills
    * @return 当前时间格式化后的字符
    */
    public static String getDate(String template, long time) {
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        Date date = new Date(time);
        return format.format(date);
    }

}
