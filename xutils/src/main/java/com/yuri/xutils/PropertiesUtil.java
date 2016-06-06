package com.yuri.xutils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtil {

    //文件
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fb44fb6518e94ab7bc432b5238679ec6";

    public static String getProperty(String name) {
        Properties properties = loadProperties(FILE_PATH);
        return properties.getProperty(name, "");
    }

    public synchronized static void saveProperty(String name, String value) {
        if (value == null) {
            return;
        }
        Properties properties = loadProperties(FILE_PATH);
        properties.setProperty(name, value);
        saveProperties(FILE_PATH, properties);
    }

    private static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream s = new FileInputStream(filePath);
                properties.load(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    private static void saveProperties(String filePath, Properties properties) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentFile = new File(filePath).getParentFile();
            if (!parentFile.exists()) {
                boolean result = parentFile.mkdirs();
                if (!result) {
                    return;
                }
            }
        }

        try {
            FileOutputStream s = new FileOutputStream(filePath, false);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
