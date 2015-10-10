package com.maxplus.study.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxplus.study.chat.ConversationListFragment;
import com.sostudy.R;

public class MeFragment extends Fragment implements OnClickListener {
	private ImageView avtor;
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
		avtor = (ImageView) getView().findViewById(R.id.ib_avtor);
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
		case R.id.ib_avtor:
		case R.id.et_myCourse:
		case R.id.et_myNotice:
		case R.id.et_myMessage:
			ConversationListFragment convActivity = new ConversationListFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.me_fragment, convActivity);
			ft.commit();

		case R.id.et_myWork:
		case R.id.et_mySet:
		case R.id.bt_exit:

			break;

		default:
			break;
		}
	}

}
