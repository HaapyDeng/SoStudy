package com.maxplus.study.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sostudy.R;

public class MeFragment extends Fragment {

	private RelativeLayout btn_info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_me, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
		btn_info = (RelativeLayout) getActivity().findViewById(R.id.btn_info);
		btn_info.setOnClickListener(onClicker);
	}
	//111111

	private OnClickListener onClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_info:
				Intent intent = new Intent();
				intent.setClass(getActivity(), MyInfoActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	};
}
