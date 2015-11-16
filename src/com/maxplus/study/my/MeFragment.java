package com.maxplus.study.my;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.http.Header;
import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.maxplus.study.chat.ConversationListFragment;
import com.maxplus.study.login.LoginActivity;
import com.maxplus.study.utils.Base64Coder;
import com.maxplus.study.utils.HttpClient;
import com.maxplus.study.utils.NetworkUtils;
import com.sostudy.R;

public class MeFragment extends Fragment implements OnClickListener {
	private ImageButton avtor;
	private TextView course, notice, message, work, setting, user, tv_class;
	private String userName, grade;
	private Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.frag_me, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		avtor = (ImageButton) getView().findViewById(R.id.ib_avtor);
		avtor.setOnClickListener(this);
		user = (TextView) getView().findViewById(R.id.tv_name);
		tv_class = (TextView) getView().findViewById(R.id.tv_class);
		course = (TextView) getView().findViewById(R.id.et_myCourse);
		course.setOnClickListener(this);
		notice = (TextView) getView().findViewById(R.id.et_myNotice);
		notice.setOnClickListener(this);
		message = (TextView) getView().findViewById(R.id.et_myMessage);
		message.setOnClickListener(this);
		work = (TextView) getView().findViewById(R.id.et_myWork);
		work.setOnClickListener(this);
		setting = (TextView) getView().findViewById(R.id.et_mySet);
		setting.setOnClickListener(this);
		btn = (Button) getView().findViewById(R.id.bt_exit);
		btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg) {
		int id = arg.getId();
		switch (id) {
		// 头像
		case R.id.ib_avtor:
			ShowPickDialog();
			// 课程
		case R.id.et_myCourse:
			// 通知
		case R.id.et_myNotice:
			// 我的消息
		case R.id.et_myMessage:
			ConversationListFragment convActivity = new ConversationListFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.me_fragment, convActivity);
			ft.commit();
			// 我的作业
		case R.id.et_myWork:
			// 设置
		case R.id.et_mySet:
			// 退出
		case R.id.bt_exit:
			if (!NetworkUtils.checkNetWork(getActivity())) {
				Toast.makeText(getActivity(), R.string.isNotNetWork,
						Toast.LENGTH_LONG).show();
				return;
			}
			SharedPreferences sp = getActivity().getSharedPreferences(
					"userInfo", Context.MODE_PRIVATE);

			String token = sp.getString("USER_TOKEN", "");
			;
			String url = "http://www.sostudy.cn";
			String realUrl = url + "/login/logout.php" + "?Sesskey=" + token;
			HttpClient.get(realUrl, null, new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString,
							throwable);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response) {
					super.onSuccess(statusCode, headers, response);
					Intent intent = new Intent();
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				}

			});

			break;

		default:
			break;
		}
	}

	private void ShowPickDialog() {

		new AlertDialog.Builder(getActivity())
				.setTitle("设置头像")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent intent = new Intent(Intent.ACTION_PICK, null);

						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();

						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										setPicName())));
						startActivityForResult(intent, 2);
					}

					private String setPicName() {
						String picName = "picName.jpg";

						return picName;
					}
				}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/picName.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃 当前功能时，会报NullException
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);

			/**
			 * 下面的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，
			 */

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来

			String tp = new String(Base64Coder.encodeLines(b));
			// 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼

			// 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
			// Bitmap dBitmap = BitmapFactory.decodeFile(tp);
			// drawable = new BitmapDrawable(dBitmap);

			avtor.setBackgroundDrawable(drawable);
			avtor.setBackgroundDrawable(drawable);
		}
	}

}
