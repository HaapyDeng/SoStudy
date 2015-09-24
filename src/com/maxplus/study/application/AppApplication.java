package com.maxplus.study.application;

import android.app.Application;
import cn.jpush.im.android.api.JMessageClient;

import com.maxplus.study.chat.NotificationClickEventReceiver;
import com.maxplus.study.chat.SharePreferenceManager;

public class AppApplication extends Application {

	public static final int REQUESTCODE_TAKE_PHOTO = 4;
	public static final int REQUESTCODE_SELECT_PICTURE = 6;
	public static final int RESULTCODE_SELECT_PICTURE = 8;
	public static final int UPDATE_CHAT_LIST_VIEW = 10;
	public static final int REFRESH_GROUP_NAME = 3000;
	public static final int REFRESH_GROUP_NUM = 3001;
	public static final int ON_GROUP_EVENT = 3004;
	private static final String JCHAT_CONFIGS = "JChat_configs";

	public static String UPDATE_GROUP_NAME_ACTION = "cn.jpush.im.demo.activity.ACTION_UPDATE_GROUP_NAME";

	@Override
	public void onCreate() {
		super.onCreate();
		JMessageClient.init(getApplicationContext());
        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        new NotificationClickEventReceiver(getApplicationContext());

	}
}
