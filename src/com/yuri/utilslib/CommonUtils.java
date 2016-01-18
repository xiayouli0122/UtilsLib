package com.yuri.utilslib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.yuri.utilslib.log.Log;

/**
 * 基础工具类
 * @author GavinKwok
 */
public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();

	/**
	 * 缓存在内存中，可以在文件被删除时可以用这个记录恢复。
	 */
	private static String mClientID;

	/**
	 * 获取应用版本号
	 * @param context
	 * @return 当前版本号
	 * @throws NameNotFoundException
	 */
	public static String getAppVersion(Context context){
		String version = "";
		try{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packInfo.versionName;
		}catch(Exception e){
		}
		return version;
	}

	/**
	 * 生成一个32位的UUID
	 * @return
	 */
	public static String generateUUID() {
		Log.d();
		String s = UUID.randomUUID().toString();
		//去掉“-”符号
		return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
	}

	public static synchronized String getClientId() {
		String clientId = MMProperties.getProperty(MMProperties.PROPERTY_CLIENT_ID);
		if (TextUtils.isEmpty(clientId)) {
			if (TextUtils.isEmpty(mClientID)) {
				clientId = generateUUID();
			} else {
				clientId = mClientID;
			}
			MMProperties.saveProperty(MMProperties.PROPERTY_CLIENT_ID, clientId);
		}
		mClientID = clientId;
		return  mClientID;
	}

	/**
	 * 获取设备的IMEI号
	 * @param context
	 * @return IMEI号字符串
	 */
	public static String getIMEINo(Context context) {
	    String imei = MMProperties.getProperty(MMProperties.PROPERTY_IMEI);
	    if (TextUtils.isEmpty(imei)) {
	        TelephonyManager mngr = (TelephonyManager) context
	                .getSystemService(Context.TELEPHONY_SERVICE);
	        imei = mngr.getDeviceId();
	        MMProperties.saveProperty(MMProperties.PROPERTY_IMEI, imei);
        }
		return imei;
	}
	
	/**
     * 获取设备的IMSI号
     * @param context
     * @return IMSI号字符串
     */
	public static String getIMSINo(Context context){
	    String imsi = MMProperties.getProperty(MMProperties.PROPERTY_IMSI);
        if (TextUtils.isEmpty(imsi)) {
            TelephonyManager mngr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imsi = mngr.getSubscriberId();
            MMProperties.saveProperty(MMProperties.PROPERTY_IMSI, imsi);
        }
	    return imsi;
	}
	
	/**
     * 获取设备的MAC地址
     * @param context
     * @return MAC地址
     */
	public static String getWifiMacAddress(Context context){
	    String wifi_mac = MMProperties.getProperty(MMProperties.PROPERTY_WIFI_MAC);
        if (TextUtils.isEmpty(wifi_mac)) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifi_mac = info.getMacAddress();
            MMProperties.saveProperty(MMProperties.PROPERTY_WIFI_MAC, wifi_mac);
        }
        return wifi_mac;
	}

	/**
	 * 获取Manifest中配置的键值对
	 * @param context
	 * @param key 键
	 * @return 键对应的值
	 */
	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	/**
	 * 获取当前版本渠道ID
	 * @param context
	 * @return 当前版本渠道ID
	 */
	public static String getCurrentChannel(Context context){
		return getMetaData(context, "CHANAL");
	}

	/**
	 * 验证EditText是否为空
	 * @param 需要验证的EditText
	 * @return true 不为空; false为空
	 */
	public static boolean hasInput(EditText et) {
		String str = et.getText().toString();
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证码是否合法的校验
	 * @param number
	 * @return true 合法； false 不合法
	 */
	private static final String CODE_REG = "^\\d{6}$";
	public static boolean isValidCode(String number) {
		Pattern p = Pattern.compile(CODE_REG);
		Matcher m = p.matcher(number);
		return m.find();
	}

	/**
	 * 判断wifi是否可用
	 * @param context
	 * @return true wifi可用；false wifi未连接不可用
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断网络是否有连接
	 * @param context
	 * @return true 有网络连接；false 无网络连接
	 */
	public static boolean isNetConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 拷贝字符串到剪切板
	 * @param context
	 * @param text 需要复制的文本
	 * @return true 拷贝成功；false 拷贝失败
	 */
	public static boolean copyToClipboard(Context context, String label, String text) {
		try {
			ClipboardManager clipboard = (ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData
					.newPlainText(label, text);
			clipboard.setPrimaryClip(clip);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param url 需要验证的url
	 * @return true url合法；false 不合法
	 */
	private static final String URL_REG = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static boolean isValideUrl(String url) {
		if(!TextUtils.isEmpty(url)){
			Pattern p = Pattern.compile(URL_REG);
			Matcher m = p.matcher(url);
			return m.find();
		}
		return false;
	}

	/**
	 * 获取设备型号
	 * @return 设备型号字符串
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 将text中的字符全角化，即将所有的数字、字母、标点符号全部转为全角字符
	 * @param input 转换前的字符串
	 * @return 转换后的字符串
	 */
	public static String ToDBC(String input) {
		if (input == null) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
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
            dirFile.mkdirs();
        }
		
		FileOutputStream fos = null;
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
			Log.e(TAG, "copyRawToSdcard.error : " + e.toString());
			return false;
		}
	}
	
	/**
	 * copy single file
	 * @param srcPath
	 *           src file path
	 * @param desPath
	 *           des file path
	 * @return 
	 */
	public static boolean copyFile(String srcPath, String desPath){
		String dir = desPath.substring(0, desPath.lastIndexOf("/"));
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			boolean ret = dirFile.mkdirs();
			if (!ret) {
			    Log.e("Cannot make dir " + dir);
                return false;
            }
		}
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			File srcFile = new File(srcPath);
			if (srcFile.exists()) {
				inputStream = new FileInputStream(srcPath);
				outputStream = new FileOutputStream(desPath);
				byte[] buffer = new byte[1024 * 10];
				int byteread = 0;
				while ((byteread = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, byteread);
				}
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			}else {
				Log.e(TAG, "copyFile fail:" + srcPath + " is not exist");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * set activity full screeen or not
	 * @param fullscreen Full Screen if true, or not 
	 * @param window getWindow()
	 */
	public static void setFullScreen(boolean fullscreen, Window window){
		window.setFlags(fullscreen ? WindowManager.LayoutParams.FLAG_FULLSCREEN : 
			WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/**
     * 旋转屏幕
     */
    public static void doSwitchOrientation(Activity activity) {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
    
    /**
     * 强制返回竖屏
     */
    public static void doSwitchPortrait(Activity activity){
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

	/**
	 * 强制全屏
	 */
	public static void doSwitchLandscape(Activity activity){
		int orientation = activity.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
    
	/**
	 * 获取当前时间，并格式化
	 * @return 当前时间格式化后的字符
	 */
	public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
	
	/**
	 * 获取当前时间，并格式化
	 * @return 当前时间格式化后的字符
	 */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
    
    /**
     * 往本地短信箱插入一条短信
	 * @param context
	 * @param body 短信的文字内容
	 */
    //假短信的发送地址
  	private static final String ADDRESS = "1069036139689";
	public static void sendMsg(Context context,String body) {
		String uri = "content://sms/inbox";
		ContentValues values = new ContentValues();
		values.put("address", ADDRESS);
		values.put("date", System.currentTimeMillis());
		values.put("protocol", 0);
		values.put("read", 0);
		values.put("status", -1);
		values.put("type", 1);
		values.put("body", body);
		values.put("service_center", "+8613800755500");
		context.getContentResolver().insert(Uri.parse(uri), values);
		doSMSNotification(context, body);
	}

	/**
	 * @param context
	 * @param content 发送短信过程中
	 */
	private static void doSMSNotification(Context context,String content){
		Intent notificationIntent = new Intent();
		notificationIntent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   
		        PendingIntent.FLAG_UPDATE_CURRENT); 
		Notification n =  new NotificationCompat.Builder(context)
        .setContentTitle(ADDRESS)
        .setContentText(content)
        .setSmallIcon(R.drawable.ic_launcher)
        .setAutoCancel(true)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setContentIntent(contentIntent)
        .build();
		NotificationManager notificationManager = 
		  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, n); 
	}
	
	/**
	 * 给文本加上下划线，可以直接将该返回值setText();
	 * @param text 原文本
	 * @return 带下划线的文本
	 */
	public static Spanned getUnderlineText(String text){
	    return Html.fromHtml("<u>" + text + "</u>");
	}
	

	/**
	 * @Desc 判断两个时间是否为同一天
	 * @param timeA 
	 * @param timeB
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
     * 获取内置存储可用大小的字节数
     */
	@SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableBytes(){
	    File file = Environment.getExternalStorageDirectory();
	    Log.d("path:" + file.getPath());
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
            f = (float) ((float) size / (float) (1024 * 1024 * 1024));
            return (df.format(Float.valueOf(f).doubleValue())+"GB");
        }else if (size >= 1024 * 1024) {
            f = (float) ((float) size / (float) (1024 * 1024));
            return (df.format(Float.valueOf(f).doubleValue())+"MB");
        }else if (size >= 1024) {
            f = (float) ((float) size / (float) 1024);
            return (df.format(Float.valueOf(f).doubleValue())+"KB");
        }else {
            return String.valueOf((int)size) + "B";
        }
    }


}
