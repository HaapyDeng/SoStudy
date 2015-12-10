package com.maxplus.study.friend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.maxplus.study.course.CourseFragment;
import com.sostudy.R;

public class SwitchFragment extends Fragment {

	private Button btn_message, btn_call;

	private FriendFragment covFragment;
	private ConversationListFragment messageFragment;

	public static final int MESSAGE_FRAGMENT_TYPE = 1;
	public static final int CALL_FRAGMENT_TYPE = 2;
	public int currentFragmentType = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_switch, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		btn_message = (Button) getActivity().findViewById(R.id.btn_message);
		btn_call = (Button) getActivity().findViewById(R.id.btn_call);
		btn_message.setOnClickListener(onClicker);
		btn_call.setOnClickListener(onClicker);

		FragmentManager fragmentManager = getFragmentManager();
		if (savedInstanceState != null) {
			int type = savedInstanceState.getInt("currentFragmentType");
			messageFragment = (ConversationListFragment) fragmentManager
					.findFragmentByTag("message");
			covFragment = (FriendFragment) fragmentManager
					.findFragmentByTag("call");
			if (type > 0)
				loadFragment(type);
		} else {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment mainFragment = fragmentManager
					.findFragmentByTag("message");
			if (mainFragment != null) {
				transaction.replace(R.id.fl_content, mainFragment);
				transaction.commit();
			} else {
				loadFragment(MESSAGE_FRAGMENT_TYPE);
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
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
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (type == CALL_FRAGMENT_TYPE) {
			if (covFragment == null) {
				covFragment = new FriendFragment();

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
				messageFragment = new ConversationListFragment();
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
				btn_message.setTextColor(Color.parseColor("#799dfe"));
				btn_call.setTextColor(Color.parseColor("#333333"));
				btn_message.setBackgroundResource(R.drawable.switchbuttonstyle);
				btn_call.setBackgroundResource(R.drawable.switchbuttonstyle2);
				switchFragment(MESSAGE_FRAGMENT_TYPE);

				break;
			case R.id.btn_call:

				btn_message.setTextColor(Color.parseColor("#333333"));
				btn_call.setTextColor(Color.parseColor("#799dfe"));
				btn_message
						.setBackgroundResource(R.drawable.switchbuttonstyle2);
				btn_call.setBackgroundResource(R.drawable.switchbuttonstyle);
				switchFragment(CALL_FRAGMENT_TYPE);

				break;

			}
		}
	};

}
