package com.maxplus.study.friend;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

import com.maxplus.study.chat.HandleResponseCode;
import com.sostudy.R;

public class SwitchFragment extends Fragment {

	private Button btn_message, btn_call;

	private FriendFragment covFragment;
	private ConversationListFragment messageFragment;
	private ConversationListController mController;
	public static final int MESSAGE_FRAGMENT_TYPE = 1;
	public static final int CALL_FRAGMENT_TYPE = 2;
	public int currentFragmentType = -1;
	private ImageButton imageButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_switch, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		btn_message = (Button) getActivity().findViewById(R.id.btn_message);
		btn_call = (Button) getActivity().findViewById(R.id.btn_call);
		btn_message.setOnClickListener(onClicker);
		btn_call.setOnClickListener(onClicker);
		imageButton = (ImageButton) getActivity().findViewById(
				R.id.add_friend_btn);
		imageButton.setOnClickListener(onClicker);

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
			case R.id.add_friend_btn:
				addFriend();
			}
		}

		private void addFriend() {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			final View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.dialog_add_friend_to_conv_list, null);
			builder.setView(view);
			final Dialog dialog = builder.create();
			dialog.show();
			final EditText userNameEt = (EditText) view
					.findViewById(R.id.user_name_et);
			final Button cancel = (Button) view.findViewById(R.id.cancel_btn);
			final Button commit = (Button) view.findViewById(R.id.commit_btn);
			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					switch (view.getId()) {
					case R.id.cancel_btn:
						dialog.cancel();
						break;
					case R.id.commit_btn:
						final String targetID = userNameEt.getText().toString()
								.trim();
						Log.i("MenuItemController", "targetID " + targetID);
						if (TextUtils.isEmpty(targetID)) {
							Toast.makeText(
									getActivity(),
									getString(R.string.username_not_null_toast),
									Toast.LENGTH_SHORT).show();
							break;
						} else if (targetID.equals(JMessageClient.getMyInfo()
								.getUserName())
								|| targetID.equals(JMessageClient.getMyInfo()
										.getNickname())) {
							Toast.makeText(getActivity(),
									getString(R.string.user_add_self_toast),
									Toast.LENGTH_SHORT).show();
							return;
						} else {
							// mLoadingDialog =
							// mLD.createLoadingDialog(mContext.getActivity(),
							// mContext.getString(R.string.adding_hint));
							// mLoadingDialog.show();
							// dismissSoftInput();
							JMessageClient.getUserInfo(targetID,
									new GetUserInfoCallback() {
										@Override
										public void gotResult(final int status,
												String desc,
												final UserInfo userInfo) {
											// if (mLoadingDialog != null)
											// mLoadingDialog.dismiss();
											if (status == 0) {
												List<Conversation> list = JMessageClient
														.getConversationList();
												Conversation conv = Conversation
														.createConversation(
																ConversationType.single,
																targetID);
												list.add(conv);

												dialog.cancel();
											} else {
												HandleResponseCode.onHandle(
														getActivity(), status,
														true);
											}
										}
									});

						}
						break;
					}
				}
			};
			cancel.setOnClickListener(listener);
			commit.setOnClickListener(listener);

		}
	};

}
