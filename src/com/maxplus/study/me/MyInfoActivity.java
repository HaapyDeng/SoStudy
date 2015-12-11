package com.maxplus.study.me;

import com.sostudy.R;
import com.sostudy.R.layout;
import com.sostudy.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MyInfoActivity extends Activity {

	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		initView();
	}

	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(cliker);

	}

	private OnClickListener cliker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.btn_back:
				finish();
				break;

			default:
				break;
			}
		}
	};

}
