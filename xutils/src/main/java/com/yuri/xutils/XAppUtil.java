package com.yuri.xutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些没法单独命名的统一放到这里
 */
public class XAppUtil {

    /**
     * 生成一个32位的UUID
     */
    public static String generateUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 获取Manifest中配置的键值对
     * @param context context
     * @param key     键
     * @return 键对应的值，如果返回null，表示未读取到
     */
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = applicationInfo.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取设备型号
     * @return 设备型号字符串
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**验证手机号码是否合法的正则表达式*/
    private static final String PHONE_REG = "^1[3|4|5|7|8][0-9]\\d{8}$";
    /**
     * 验证手机号是否合法
     * @param number 需要验证的手机号码
     * @return true 手机号码合法；false手机号码不合法
     */
    public static boolean isValidPhoneNumber(String number) {
        Pattern p = Pattern.compile(PHONE_REG);
        Matcher m = p.matcher(number);
        return m.find();
    }
}
