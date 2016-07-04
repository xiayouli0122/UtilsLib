package com.yuri.xutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static String getDate(long time, String template) {
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        Date date = new Date(time);
        return format.format(date);
    }

    /**
     * 判断两个时间是否为同一天
     * @param timeA time in mills
     * @param timeB time in mills
     * @return true 两个时间为同一天；false：非同一天
     */
    public static boolean isSameDay(long timeA, long timeB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTimeInMillis(timeA);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTimeInMillis(timeB);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断指定时间是否就是今天的日期
     * @param time time in mills
     * @return true toady，else not
     */
    public static boolean isToady(long time) {
        return time  > 0 && isSameDay(time, System.currentTimeMillis());
    }

}
