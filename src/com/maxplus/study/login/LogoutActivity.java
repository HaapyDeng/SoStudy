package com.maxplus.study.login;

import cn.jpush.im.android.api.JMessageClient;

import com.sostudy.R;
import com.sostudy.R.layout;
import com.sostudy.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LogoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);

		// 退出IM登录状态
		JMessageClient.logout();
		
	}

}
