package com.maxplus.study.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import cn.jpush.android.api.JPushInterface;

import com.sostudy.R;

public class MainActivity extends FragmentActivity {
	private MainController mMainController;
	private MainView mMainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMainView = (MainView) findViewById(R.id.main_view);
		mMainView.initModule();
		mMainController = new MainController(mMainView, this);

		mMainView.setOnClickListener(mMainController);
		mMainView.setOnPageChangeListener(mMainController);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public FragmentManager getSupportFragmentManger() {
		// TODO Auto-generated method stub
		return getSupportFragmentManager();
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_CANCELED) {
//			return;
//		}
//		if (requestCode == JPushDemoApplication.REQUESTCODE_TAKE_PHOTO) {
//			String path = mMainController.getPhotoPath();
//			if (path != null)
//				mMainController.calculateAvatar(path);
//		} else if (requestCode == JPushDemoApplication.REQUESTCODE_SELECT_PICTURE) {
//			if (data != null) {
//				Uri selectedImg = data.getData();
//				if (selectedImg != null) {
//					Cursor cursor = this.getContentResolver().query(
//							selectedImg, null, null, null, null);
//					if (null == cursor || !cursor.moveToFirst()) {
//						Toast.makeText(this,
//								this.getString(R.string.picture_not_found),
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
//					int columnIndex = cursor.getColumnIndex("_data");
//					String path = cursor.getString(columnIndex);
//					if (path != null) {
//						File file = new File(path);
//						if (file == null || !file.exists()) {
//							Toast.makeText(this,
//									this.getString(R.string.picture_not_found),
//									Toast.LENGTH_SHORT).show();
//							return;
//						}
//					}
//					cursor.close();
//					mMainController.calculateAvatar(path);
//				}
//			}
//		}
//	}

}
