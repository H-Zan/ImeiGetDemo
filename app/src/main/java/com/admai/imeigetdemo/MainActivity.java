package com.admai.imeigetdemo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MAIN_IMEI";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getImeis();
	}

	@SuppressLint("HardwareIds")
	private void getImeis() {

		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

		//默认方法, 只能获取到 imei1
		String imei = tm.getDeviceId();
		Log.w(TAG, "imei: " + imei);

		//api 23 以上
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String imei1 = tm.getDeviceId(0);
			String imei2 = tm.getDeviceId(1);
			Log.e(TAG, "imei1: " + imei1 + ", imei2: " + imei2);
		}

		//5.0 以上
		String imei1 = ImeiUtil.getImei1(this); //卡槽1
		String imei2 = ImeiUtil.getImei2(this); //卡槽2
		Log.e(TAG, "imei1: " + imei1 + ", imei2: " + imei2);
	}

}
