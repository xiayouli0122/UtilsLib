package com.yuri.utilslib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;

public class StorageUtils {

	/**
	 * get sdcard paths,默认第一个是内置sdcard
	 * @param context
	 * @returns sdcard paths
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String[] getVolumePaths(Context context) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		String[] paths = null;
		StorageManager sm = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);

		Method method = sm.getClass().getMethod("getVolumePaths");
		paths = (String[]) method.invoke(sm);
		return paths;
	}
}
