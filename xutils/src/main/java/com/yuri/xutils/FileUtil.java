package com.yuri.xutils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * File相关Util
 */
public class FileUtil {

    /**
     * get parent path
     * @param path current file path
     * @return parent path
     */
    public static String getParentPath(String path){
        File file = new File(path);
        return file.getParent();
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * 将Raw中的文件拷贝到sdcard中
     * @param desPath sdcard 目标文件路径
     * @param name raw文件名称
     */
    public static boolean copyRawToSdcard(Context context, String desPath, String name){
        String dir = desPath.substring(0, desPath.lastIndexOf("/"));
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            boolean ret = dirFile.mkdirs();
            if (!ret) {
                return false;
            }
        }

        FileOutputStream fos;
        try {
            byte[] buffer = new byte[4096];
            int id = context.getResources().getIdentifier(name, "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(id);
            fos = new FileOutputStream(desPath);
            int count;
            count = inputStream.read(buffer);
            while (count > 0) {
                fos.write(buffer);
                count = inputStream.read(buffer);
            }
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取内置存储可用大小的字节数
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableBytes(){
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getAvailableBytes();
        }
        long blocksize = statFs.getBlockCount();
        long availableblocks = statFs.getAvailableBlocks();
        return availableblocks * blocksize;
    }
    /**
     * 获取内置存储可用大小
     */
    public static String getAvailableFormatSize(){
        return getFormatSize(getAvailableBytes());
    }

    /**
     * byte convert
     * @param size like 3232332
     * @return like 3.23M
     */
    public static String getFormatSize(long size){
        DecimalFormat df = new DecimalFormat("###.##");
        float f;
        if (size >= 1024 * 1024 * 1024){
            f = (float) size / (float) (1024 * 1024 * 1024);
            return (df.format(Float.valueOf(f).doubleValue())+"GB");
        }else if (size >= 1024 * 1024) {
            f = (float) size / (float) (1024 * 1024);
            return (df.format(Float.valueOf(f).doubleValue())+"MB");
        }else if (size >= 1024) {
            f = (float) size / (float) 1024;
            return (df.format(Float.valueOf(f).doubleValue())+"KB");
        }else {
            return String.valueOf((int)size) + "B";
        }
    }


}
