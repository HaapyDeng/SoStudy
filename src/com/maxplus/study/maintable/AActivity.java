package com.maxplus.study.maintable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.maxplus.study.application.AppApplication;
import com.sostudy.R;

public class AActivity extends Activity {

	private AppApplication app;
	private String name;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a);
		tv = (TextView) findViewById(R.id.tv1);
	}

}
