package com.yuri.xutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;

import java.util.List;

/**
 * 系统自带Intent相关
 */
public class IntentUtil {

    /**
     * 跳转到app的应用信息界面
     */
    public static void toAppInfo(Context context, String packageName) {
        //跳转到应用信息界面
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + packageName);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 跳转到应用市场
     *
     * @param context context
     */
    public static void toAppMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转应用宝迪乐姆界面
     *
     * @param context context
     */
    public static void toTencentMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.tencent.android.qqdownloader");
        context.startActivity(intent);
    }


    /**
     * 获取跳转到应用市场的Intent
     *
     * @param context context
     * @return Intent marketIntent
     */
    public static Intent getMarketIntent(Context context) {
        StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=");
        String str = context.getPackageName();
        localStringBuilder.append(str);
        Uri localUri = Uri.parse(localStringBuilder.toString());
        return new Intent(Intent.ACTION_VIEW, localUri);
    }

    public static boolean hasIntent(Context context, Intent intent) {
        List<ResolveInfo> localList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        return localList != null && localList.size() > 0;
    }

    /**
     * 判断指定包名的app 是否安装
     *
     * @param context     context
     * @param packageName 包名
     * @return 是否安装
     */
    private static boolean isPackageInstalled(Context context, String packageName) {
        Intent queryIntent = new Intent();
        queryIntent.setPackage(packageName);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(queryIntent, 0);
        return list != null && !list.isEmpty();
    }

}
