package com.yuri.utilslib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.os.Environment;

import com.yuri.utilslib.log.Log;


public class MMProperties {
    //文件
    private static final String FILE_PATH =Environment.getExternalStorageDirectory().getAbsolutePath() + "/fb44fb6518e94ab7bc432b5238679ec6";

    public static final String PROPERTY_CLIENT_ID = "client_id";
    public static final String PROPERTY_IMEI = "imei";
    public static final String PROPERTY_IMSI = "imsi";
    public static final String PROPERTY_WIFI_MAC = "wifi_mac";

    public static String getProperty(String name) {
        Properties properties = loadProperties(FILE_PATH);
        return properties.getProperty(name, "");
    }

    public synchronized static void saveProperty(String name, String value) {
        if (value == null) {
            Log.d("value is null, ignore.");
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
                Log.e("error: " + e);
            }
        } else {
            Log.d("No properties file.");
        }
        return properties;
    }

    private static void saveProperties(String filePath, Properties properties) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentFile = new File(filePath).getParentFile();
            if (!parentFile.exists()) {
                boolean result = parentFile.mkdirs();
                if (result) {
                    Log.d("Create properties file dir success.");
                } else {
                    Log.e("Create properties file dir fail.");
                    return;
                }
            }
        }

        try {
            FileOutputStream s = new FileOutputStream(filePath, false);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error: " + e);
        }
    }
}
