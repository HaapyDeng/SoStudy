package com.maxplus.study.maintable;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.sostudy.R;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	@SuppressWarnings("unused")
	private RadioGroup rgTab;
	private TabHost th;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	GestureDetector gestureDetector;
	View.OnTouchListener listener;
	int currentView = 0;
	private static int maxTabIndex = 3;

	static int tab = -1;
	static final int tab1 = 0;
	static final int tab2 = 1;
	static final int tab3 = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs);
		tab = tab1;

		initTab();
		// 监听手指滑动事件，滑动切换activity
		gestureDetector = new GestureDetector(new MyListener());

		listener = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		};
	}

	class MyListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			try {

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					if (currentView == maxTabIndex) {
						currentView = 0;
					} else {
						currentView++;
					}
					th.setCurrentTab(currentView);
					initTab(currentView);

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (currentView == 0) {
						currentView = maxTabIndex;
					} else {
						currentView--;
					}
					th.setCurrentTab(currentView);
					initTab(currentView);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			return false;
		}

	}

	RadioButton button1;
	RadioButton button2;
	RadioButton button3;

	private void initTab() {
		rgTab = (RadioGroup) this.findViewById(R.id.main_radio);
		th = this.getTabHost();

		th.addTab(th.newTabSpec("TS_HOME1").setIndicator("TS_HOME1")
				.setContent(new Intent(MainTabActivity.this, AActivity.class)));
		th.addTab(th.newTabSpec("TS_HOME2").setIndicator("TS_HOME2")
				.setContent(new Intent(MainTabActivity.this, BActivity.class)));
		th.addTab(th.newTabSpec("TS_HOME3").setIndicator("TS_HOME3")
				.setContent(new Intent(MainTabActivity.this, CActivity.class)));
		button1 = (RadioButton) findViewById(R.id.radio_button0);
		button2 = (RadioButton) findViewById(R.id.radio_button1);
		button3 = (RadioButton) findViewById(R.id.radio_button2);
		button1.setTextColor(Color.BLUE);
		th.setCurrentTab(0);

		// TODO Auto-generated method stub

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tab == tab1) {
					return;
				}
				th.setCurrentTabByTag("TS_HOME1");
				button1.setTextColor(Color.BLUE);
				setOther(tab1);
			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (tab == tab2) {
					return;
				}
				th.setCurrentTabByTag("TS_HOME2");
				button2.setTextColor(Color.BLUE);
				setOther(tab2);
			}
		});

		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				th.setCurrentTabByTag("TS_HOME3");
				button3.setTextColor(Color.BLUE);
				setOther(tab3);
			}
		});

	}

	private void setOther(int state) {
		switch (tab) {
		case tab1:
			button1.setTextColor(Color.BLACK);
			break;
		case tab2:
			button2.setTextColor(Color.BLACK);
			break;
		case tab3:
			button3.setTextColor(Color.BLACK);
			break;
		default:
			break;
		}

		tab = state;
	}

	private void initTab(int tabIndex) {
		switch (tabIndex) {
		case tab1:
			button1.setTextColor(Color.BLUE);
			setOther(tab1);
			break;
		case tab2:
			button2.setTextColor(Color.BLUE);
			setOther(tab2);
			break;
		case tab3:
			setOther(tab3);
			button3.setTextColor(Color.BLUE);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (gestureDetector.onTouchEvent(ev)) {
			ev.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
