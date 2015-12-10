package com.maxplus.study.friend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.maxplus.study.course.CourseFragment;
import com.sostudy.R;

public class SwitchActivity extends FragmentActivity {

	private Button btn_message,btn_call;
	
	private ConversationListFragment covFragment;
    private FriendFragment messageFragment;
	
	public static final int MESSAGE_FRAGMENT_TYPE = 1;
	public static final int CALL_FRAGMENT_TYPE = 2;
	public int currentFragmentType = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_switch);
		
		btn_message = (Button)findViewById(R.id.btn_message);
		btn_call = (Button)findViewById(R.id.btn_call);
		btn_message.setOnClickListener(onClicker);
		btn_call.setOnClickListener(onClicker);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (savedInstanceState != null) {
            int type = savedInstanceState.getInt("currentFragmentType");
            messageFragment = (FriendFragment)fragmentManager.findFragmentByTag("message");
            covFragment = (ConversationListFragment)fragmentManager.findFragmentByTag("call");
            if(type > 0)
            	loadFragment(type);
		} else {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment mainFragment = fragmentManager.findFragmentByTag("message");
			if (mainFragment != null) {
				transaction.replace(R.id.fl_content, mainFragment);
				transaction.commit();
			} else {
				loadFragment(MESSAGE_FRAGMENT_TYPE);
			}
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("lastFragmentTag", currentFragmentType);
	}
	
	private void switchFragment(int type) {
		switch (type) {
		case MESSAGE_FRAGMENT_TYPE:
			loadFragment(MESSAGE_FRAGMENT_TYPE);
			break;
		case CALL_FRAGMENT_TYPE:
			loadFragment(CALL_FRAGMENT_TYPE);
			break;
		}
		
	}

	private void loadFragment(int type) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (type == CALL_FRAGMENT_TYPE) {
			if (covFragment == null) {
				covFragment = new ConversationListFragment();

				transaction.add(R.id.fl_content, covFragment, "zhishi");
			} else {
				transaction.show(covFragment);
			}
			if (messageFragment != null) {
				transaction.hide(messageFragment);
			}
			currentFragmentType = MESSAGE_FRAGMENT_TYPE;
		} else {
			if (messageFragment == null) {
				messageFragment = new FriendFragment();
				transaction.add(R.id.fl_content, messageFragment, "wenda");
			} else {
				transaction.show(messageFragment);
			}
			if (covFragment != null) {
				transaction.hide(covFragment);
			}
			currentFragmentType = CALL_FRAGMENT_TYPE;
		}
		transaction.commitAllowingStateLoss();
	}
	
	private OnClickListener onClicker = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_message:
				btn_message.setTextColor(Color.parseColor("#df3031"));
				btn_call.setTextColor(Color.WHITE);
				btn_message
						.setBackgroundResource(R.drawable.switchbuttonstyle);
				btn_call
						.setBackgroundResource(R.drawable.switchbuttonstyle2);
				switchFragment(MESSAGE_FRAGMENT_TYPE);
				
				break;
			case R.id.btn_call:
				
				btn_message.setTextColor(Color.WHITE);
				btn_call.setTextColor(Color.parseColor("#df3031"));
				btn_message
						.setBackgroundResource(R.drawable.switchbuttonstyle2);
				btn_call
						.setBackgroundResource(R.drawable.switchbuttonstyle);
				switchFragment(CALL_FRAGMENT_TYPE);
				
				break;
			
			}
		}
	};

}

