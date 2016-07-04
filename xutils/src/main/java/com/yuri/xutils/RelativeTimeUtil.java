package com.yuri.xutils;

import java.util.Calendar;

/**
 * 模仿微博计算消息的日期格式化
 * Created by Yuri on 2016/6/29.
 */
public class RelativeTimeUtil {

    private static final long HALF_MINUTE = 30L;
    private static final long ONE_MINUTE = 60L;
    private static final long ONE_HOUR = 3600L;
    private static final long ONE_DAY = 86400L;
    private static final long ONE_WEEK = 604800L;
    private static final long ONE_YEAR = 31536000L;

    private static final String JUST_NOW = "刚刚";
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String HALF_MINUTE_AGO = "半分钟前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String TODAY = "今天";
    private static final String YESTERDAY = "昨天";
    private static final String ONE_DAY_AGO = "天前";

    public static String format(long time) {
        long delta = System.currentTimeMillis() - time;
        String result;
        long seconds = toSeconds(delta);
        if (TimeUtil.isToady(time)) {
            //是否是今天
            if (seconds < 15) {
                result = JUST_NOW;
            } else if (seconds >= 15 && seconds < HALF_MINUTE) {
                result = seconds + ONE_SECOND_AGO;
            } else if (seconds >= HALF_MINUTE && seconds < ONE_MINUTE) {
                result = HALF_MINUTE_AGO;
            } else if (seconds >= ONE_MINUTE && seconds < ONE_HOUR) {
                long minute = toMinutes(seconds);
                result = minute + ONE_MINUTE_AGO;
            } else {
                long hour = toHours(seconds);
                if (hour <= 3) {
                    result = hour + ONE_HOUR_AGO;
                } else {
                    result = TODAY + TimeUtil.getDate(time, "HH:mm");
                }
            }
        } else {
            //不是同一天
            //得到明天00:00的时间戳
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            long newTime = calendar.getTimeInMillis();

            delta = newTime - time;
            long newSeconds = toSeconds(delta);

            if (seconds < ONE_DAY + newSeconds) {
                result = YESTERDAY + TimeUtil.getDate(time, "HH:mm");
            } else if (seconds >= ONE_DAY + newSeconds && seconds < ONE_WEEK) {
                long dayDelta = toDays(seconds);
                result = dayDelta + ONE_DAY_AGO + TimeUtil.getDate(time, "HH:mm");
            } else if (seconds >= ONE_WEEK && seconds < ONE_YEAR) {
                result = TimeUtil.getDate(time, "MM-dd");
            } else {
                result = TimeUtil.getDate(time, "yyyy-MM-dd");
            }
        }
        return result;
    }

    private static long toSeconds(long time) {
        return time / 1000L;
    }

    private static long toMinutes(long seconds) {
        return seconds / 60L;
    }

    private static long toHours(long seconds) {
        return (seconds / 60) / 60;
    }

    private static long toDays(long seconds) {
        return ((seconds / 60) / 60) / 24;
    }
}
