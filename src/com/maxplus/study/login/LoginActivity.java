package com.maxplus.study.login;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sostudy.R;
import com.maxplus.study.application.AppApplication;
import com.maxplus.study.maintable.MainTabActivity;

public class LoginActivity extends Activity {
	private SharedPreferences sp;
	private EditText edt_UserName;
	private EditText edt_Password;
	private LinearLayout ll, ll2;
	// private ImageView img;
	private Button btn_login;
	private String userName;
	private String password;
	private CheckBox rem_pw;
	private AppApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		// 加载视图
		inivtView();
	}

	private void inivtView() {

		edt_UserName = (EditText) findViewById(R.id.ed_UserName);
		edt_Password = (EditText) findViewById(R.id.ed_Password);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		btn_login = (Button) findViewById(R.id.login);
		ll = (LinearLayout) findViewById(R.id.ll_1);
		ll2 = (LinearLayout) findViewById(R.id.imgView);
		Animation animation = (Animation) AnimationUtils.loadAnimation(
				LoginActivity.this, R.anim.translate);
		ll2.startAnimation(animation);
		ll2.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Animation animation2 = (Animation) AnimationUtils
						.loadAnimation(LoginActivity.this, R.anim.translate);

				ll.startAnimation(animation2);
				ll.setVisibility(View.VISIBLE);
			}

		}, 100);

		// 判断记住密码多选框的状态
		if (sp.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			rem_pw.setChecked(true);
			edt_UserName.setText(sp.getString("USER_NAME", ""));
			edt_Password.setText(sp.getString("PASSWORD", ""));
			// 跳转界面
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainTabActivity.class);
			startActivity(intent);

		}
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
				String url = "http://www.sostudy.cn/login/index.php";
				RequestParams param = new RequestParams();
				param.put(userName, userName);
				param.put(password, password);
				// 发起登录请求post
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, param, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						// 请求成功，跳转到主界面...
						app = new AppApplication();

						System.out.println("成功！");
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this,
								MainTabActivity.class);
						startActivity(intent);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						System.out.print("连接失败");
						return;

					}
				});
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

		// 监听自动登录多选框事件
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

}
