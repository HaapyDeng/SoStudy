package com.maxplus.study.maintable;

import cn.jpush.im.android.api.JMessageClient;

import com.sostudy.R;

import android.app.Activity;
import android.os.Bundle;

public class ConversationListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		JMessageClient.registerEventReceiver(this);
		setContentView(R.layout.activity_conversation_list);
	}

}
