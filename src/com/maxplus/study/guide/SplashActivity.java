package com.maxplus.study.guide;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.maxplus.study.login.LoginActivity;
import com.maxplus.study.main.MainActivity;
import com.maxplus.study.utils.HttpClient;
import com.maxplus.study.utils.NetworkUtils;
import com.sostudy.R;

/**
 * 
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 * 
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 * 
 * 
 */
public class SplashActivity extends Activity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private String userName, password, token;
	private String service = "moodle_mobile_app";

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	private void goHome() {
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		userName = sp.getString("USER_NAME", "");
		password = sp.getString("PASSWORD", "");
		if (!NetworkUtils.checkNetWork(SplashActivity.this)) {
			Toast.makeText(SplashActivity.this, R.string.isNotNetWork,
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
		if (sp.getBoolean("ISCHECK", false) && (userName != "")
				&& (password != "")) {
			// 记住用户名和密码自动登录
			doLoginPost(userName, password, service);
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}

	}

	private void doLoginPost(final String userName, final String password,
			String service) {
		// TODO Auto-generated method stub

		// 判断手机网络是否连接
		if (!NetworkUtils.checkNetWork(SplashActivity.this)) {
			Toast.makeText(SplashActivity.this, R.string.isNotNetWork,
					Toast.LENGTH_LONG).show();
			return;
		}
		String url = "http://www.sostudy.cn/login/token.php";
		RequestParams param = new RequestParams();
		param.put("username", userName);
		param.put("password", password);
		param.put("service", service);
		// 发起登录请求post
		Log.i("Url", "" + url);
		HttpClient.post(url, param, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.i("response", "" + response);
				try {
					if (response.has("error")) {
						Toast.makeText(SplashActivity.this,
								response.getString("error"), Toast.LENGTH_LONG)
								.show();
						// 清空首选项中的用户名和密码，跳转到登录界面
						SharedPreferences sp = getSharedPreferences("userInfo",
								Context.MODE_PRIVATE);
						Editor ed = sp.edit();
						ed.putString("USER_NAME", "");
						ed.putString("PASSWORD", "");
						ed.putBoolean("ISCHECK", true);
						ed.commit();
						Intent intent = new Intent(SplashActivity.this,
								LoginActivity.class);
						SplashActivity.this.startActivity(intent);
						SplashActivity.this.finish();

					} else if (response.has("token")) {
						token = response.getString("token");
						Toast.makeText(SplashActivity.this,
								R.string.successLogin, Toast.LENGTH_SHORT)
								.show();
						// 登录到IM聊天服务器
						LoginToIm(userName, password);
					} else {
						Toast.makeText(SplashActivity.this, R.string.tryLater,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private void LoginToIm(String userName, String password) {
				// TODO Auto-generated method stub
				JMessageClient.login(userName, password, new BasicCallback() {
					@Override
					public void gotResult(final int status, String desc) {
						if (status == 0) {
							// 登录到IM成功，跳转到主界面
							Intent intent = new Intent();
							intent.setClass(SplashActivity.this,
									MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(SplashActivity.this,
									R.string.tryLater, Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.i("error", "" + errorResponse);
				Toast.makeText(SplashActivity.this, R.string.tryLater,
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
}
