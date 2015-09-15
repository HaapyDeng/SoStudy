package com.maxplus.study.application;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

public class AppApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("JMessageDemoApplication", "Application onCreate");
	    JMessageClient.init(getApplicationContext());
	    JPushInterface.setDebugMode(true);

	}

}
