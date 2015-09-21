package com.maxplus.study.application;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

import com.maxplus.study.chat.NotificationClickEventReceiver;

public class AppApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		JMessageClient.init(getApplicationContext());
		JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        new NotificationClickEventReceiver(getApplicationContext());
		JPushInterface.setDebugMode(true);

	}
}
