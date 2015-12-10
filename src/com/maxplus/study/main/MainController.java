package com.maxplus.study.main;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxplus.study.course.CourseFragment;
import com.maxplus.study.friend.SwitchFragment;
import com.maxplus.study.me.MeFragment;
import com.sostudy.R;

public class MainController implements OnClickListener, OnPageChangeListener {

	private Fragment mMeActivity;
	private MainView mMainView;
	private MainActivity mContext;
	private ProgressDialog mDialog;

	public MainController(MainView mMainView, MainActivity context) {
		this.mMainView = mMainView;
		this.mContext = context;
		setViewPager();
	}

	private void setViewPager() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		// init Fragment
		SwitchFragment convActivity = new SwitchFragment();
		Fragment contactsActivity = new CourseFragment();
		mMeActivity = new MeFragment();
		fragments.add(contactsActivity);
		fragments.add(convActivity);
		fragments.add(mMeActivity);
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
				mContext.getSupportFragmentManger(), fragments);
		mMainView.setViewPagerAdapter(viewPagerAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.actionbar_msg_btn:
			mMainView.setCurrentItem(0);
			break;
		case R.id.actionbar_contact_btn:
			mMainView.setCurrentItem(1);
			break;
		case R.id.actionbar_me_btn:
			mMainView.setCurrentItem(2);
			break;
		}
	}

	// public String getPhotoPath() {
	// return mMeActivity.getPhotoPath();
	// }

	// public void calculateAvatar(final String originPath) {
	// mDialog = new ProgressDialog(mContext);
	// mDialog.setCancelable(false);
	// mDialog.setMessage(mContext.getString(R.string.updating_avatar_hint));
	// mDialog.show();
	// // 验证图片大小，若小于720 * 1280则直接发送原图，否则压缩
	// if (BitmapLoader.verifyPictureSize(originPath))
	// updateAvatar(originPath);
	// else {
	// Bitmap bitmap = BitmapLoader.getBitmapFromFile(originPath, 720,
	// 1280);
	// String tempPath = BitmapLoader.saveBitmapToLocal(bitmap);
	// updateAvatar(tempPath);
	// }
	// }

	// private void updateAvatar(final String path) {
	// JMessageClient.updateUserAvatar(new File(path),
	// new BasicCallback(false) {
	// @Override
	// public void gotResult(final int status, final String desc) {
	// mContext.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// mDialog.dismiss();
	// if (status == 0) {
	// Log.i("MeFragment",
	// "Update avatar succeed path "
	// + path);
	// loadUserAvatar(path);
	// } else {
	// HandleResponseCode.onHandle(mContext,
	// status, false);
	// }
	// }
	// });
	// }
	// });
	// }

	// private void loadUserAvatar(String path) {
	// if (path != null)
	// mMeActivity.loadUserAvatar(path);
	// }

	@Override
	public void onPageSelected(int index) {
		// TODO Auto-generated method stub
		mMainView.setButtonColor(index);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
