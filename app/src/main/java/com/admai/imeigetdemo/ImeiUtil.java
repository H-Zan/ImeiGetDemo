package com.admai.imeigetdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by ZAN on 2017/8/11.
 * ImeiUtil.
 * <p>
 * slotId为卡槽Id，它的值为 0(卡槽1)、1(卡槽2);
 * subId为卡Id，相当于在手机卡插到手机上时，系统为卡分配的一个标识Id，这个值通过ContentProvider来获取。
 * <p>
 * 若不通过反射来获取, 则只能获取到 imei1
 */

public class ImeiUtil {


	// imei1
	public static String getImei1(Context context) {
		return getDeviceId(context, 0);
	}

	// imei2
	public static String getImei2(Context context) {
		return getDeviceId(context, 1);
	}

	// imei
	public static String getDeviceId(Context context, int soltId) {
		return (String) getPhoneInfo(soltId, "getDeviceId", context);
	}

	// imsi1
	public static String getImsi1(Context context) {
		return getSubscriberId(context, 0);
	}

	// imsi1
	public static String getImsi2(Context context) {
		return getSubscriberId(context, 1);
	}

	// imsi
	public static String getSubscriberId(Context context, int subId) {
		return (String) getPhoneInfo(subId, "getSubscriberId", context);
	}

	// sim卡 id 1
	public static int getSimId1(Context context) {
		return getSubId(context, 0);
	}

	// sim卡 id 2
	public static int getSimId2(Context context) {
		return getSubId(context, 1);
	}

	//sim卡 id
	public static int getSubId(Context context, int slotId) {
		Uri uri = Uri.parse("content://telephony/siminfo");
		Cursor cursor = null;
		ContentResolver contentResolver = context.getContentResolver();
		try {
			cursor = contentResolver.query(uri, new String[]{"_id", "sim_id"}, "sim_id = ?",
			                               new String[]{String.valueOf(slotId)}, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					return cursor.getInt(cursor.getColumnIndex("_id"));
				}
			}
		} catch (Exception e) {
			Log.d("getSubId", e.toString());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		return -1;
	}

	public static Object getPhoneInfo(int subId, String methodName, Context context) {
		Object value = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (Build.VERSION.SDK_INT >= 21) {
				Method method = tm.getClass().getMethod(methodName, getMethodParamTypes(methodName));
				if (subId >= 0) {
					value = method.invoke(tm, subId);
				}
			}
		} catch (Exception e) {
			Log.d("", e.toString());
		}
		return value;
	}

	public static Class[] getMethodParamTypes(String methodName) {
		Class[] params = null;
		try {
			Method[] methods = TelephonyManager.class.getDeclaredMethods();
			for (Method method : methods) {
				if (methodName.equals(method.getName())) {
					params = method.getParameterTypes();
					if (params.length >= 1) {
						Log.d("length:", "" + params.length);
						break;
					}
				}
			}
		} catch (Exception e) {
			Log.d("", e.toString());
		}
		return params;
	}
}
