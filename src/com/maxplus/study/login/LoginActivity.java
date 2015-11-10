package com.maxplus.study.login;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.maxplus.study.main.MainActivity;
import com.maxplus.study.regist.RegistActivity;
import com.maxplus.study.utils.HttpClient;
import com.maxplus.study.utils.NetworkUtils;
import com.sostudy.R;

public class LoginActivity extends Activity {
	private SharedPreferences sp;
	private EditText edt_UserName;
	private EditText edt_Password;
	private LinearLayout ll, ll2;
	private Button btn_login, btn_regist;
	private String userName;
	private String password;
	private CheckBox rem_pw;
	private String service = "moodle_mobile_app";
	public String token;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

		// 加载视图
		inivtView();
		// 判断记住密码多选框的状态
		// if (sp.getBoolean("ISCHECK", false)) {
		// // 设置默认是记录密码状态
		// rem_pw.setChecked(true);
		// edt_UserName.setText(sp.getString("USER_NAME", ""));
		// edt_Password.setText(sp.getString("PASSWORD", ""));
		// userName = sp.getString("USER_NAME", "");
		// password = sp.getString("PASSWORD", "");
		// // 自动登录
		// doLoginPost();
		//
		// }
	}

	private void inivtView() {

		edt_UserName = (EditText) findViewById(R.id.ed_UserName);
		edt_Password = (EditText) findViewById(R.id.ed_Password);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		btn_login = (Button) findViewById(R.id.login);
		ll = (LinearLayout) findViewById(R.id.ll_1);
		ll2 = (LinearLayout) findViewById(R.id.imgView);
		btn_regist = (Button) findViewById(R.id.register);

		// Animation animation = (Animation) AnimationUtils.loadAnimation(
		// LoginActivity.this, R.anim.translate);
		// ll2.startAnimation(animation);
		// ll2.setVisibility(View.VISIBLE);
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// Animation animation2 = (Animation) AnimationUtils
		// .loadAnimation(LoginActivity.this, R.anim.translate);
		// ll.startAnimation(animation2);
		// ll.setVisibility(View.VISIBLE);
		// }
		//
		// }, 500);

		// 监听登录事件
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				userName = edt_UserName.getText().toString();
				password = edt_Password.getText().toString();
				if (userName.equals("") || password.equals("")) {
					Toast.makeText(LoginActivity.this, R.string.notNull,
							Toast.LENGTH_LONG).show();
					return;
				}

				doLoginPost();

				// 登录成功和记住密码框为选中状态才保存用户信息
				if (rem_pw.isChecked()) {
					// 记住用户名、密码、
					Editor editor = sp.edit();
					editor.putString("USER_NAME", userName);
					editor.putString("PASSWORD", password);
					editor.commit();
				}

			}

		});
		// 监听注册按钮事件
		btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {

					System.out.println("记住密码已选中");
					sp.edit().putBoolean("ISCHECK", true).commit();

				} else {

					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}

			}
		});
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

	private void doLoginPost() {
		// 判断手机网络是否连接
		if (!NetworkUtils.checkNetWork(LoginActivity.this)) {
			Toast.makeText(LoginActivity.this, R.string.isNotNetWork,
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
		dialog = ProgressDialog.show(LoginActivity.this, null, "正在登录中，请稍候...",
				true, false);
		HttpClient.post(url, param, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.i("response", "" + response);
				try {
					if (response.has("error")) {
						Toast.makeText(LoginActivity.this,
								response.getString("error"), Toast.LENGTH_LONG)
								.show();
						dialog.dismiss();
						// 清空用户名和密码，跳转到登录界面
						SharedPreferences sp2 = getSharedPreferences(
								"userInfo", Context.MODE_PRIVATE);
						Editor ed = sp2.edit();
						ed.putString("USER_NAME", "");
						ed.putString("PASSWORD", "");
						ed.putBoolean("ISCHECK", true);
						ed.commit();
						return;

					} else if (response.has("token")) {
						token = response.getString("token");
						SharedPreferences sp3 = getSharedPreferences(
								"userInfo", Context.MODE_PRIVATE);
						Editor ed = sp3.edit();

						ed.putString("USER_TOKEN", token);
						ed.commit();
						Toast.makeText(LoginActivity.this,
								R.string.successLogin, Toast.LENGTH_SHORT)
								.show();
						// 登录到IM聊天服务器

						LoginToIm(userName, password);

					} else {
						dialog.dismiss();
						Toast.makeText(LoginActivity.this, R.string.tryLater,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dialog.dismiss();
				Log.i("error", "" + errorResponse);
				Toast.makeText(LoginActivity.this, R.string.tryLater,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 登录到IM
	private void LoginToIm(final String userName, final String password) {
		JMessageClient.login(userName, password, new BasicCallback() {
			@Override
			public void gotResult(final int status, String desc) {
				if (status == 0) {
					// 登录到IM成功，跳转到主界面
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					dialog.dismiss();
				} else {
					dialog.dismiss();
					Toast.makeText(LoginActivity.this, R.string.tryLater,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/**
		 * 注册用户名和密码到Im
		 * 
		 * JMessageClient.register(userName, password, new BasicCallback() {
		 * 
		 * @Override public void gotResult(final int status, final String desc)
		 *           { LoginActivity.this.runOnUiThread(new Runnable() {
		 * @Override public void run() { if (status == 0) {
		 *           JMessageClient.login(userName, password, new
		 *           BasicCallback() {
		 * @Override public void gotResult(final int status, String desc) { if
		 *           (status == 0) { // 登录到IM成功，跳转到主界面 Intent intent = new
		 *           Intent(); intent.setClass( LoginActivity.this,
		 *           MainTabActivity.class); startActivity(intent); } else {
		 * 
		 *           } } }); } else {
		 * 
		 *           } } }); }
		 * 
		 *           });
		 */

	}
}
