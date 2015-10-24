package com.zwbk.contacts;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private TextView tv_contact;
	private TextView tv_history;
	private TextView tv_dial;
	private LinearLayout content;

	private android.support.v4.app.FragmentManager fm;
	private android.support.v4.app.FragmentTransaction ft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		// 找到fragment_main中的textview对象，并未他们设置点击事件
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_history = (TextView) findViewById(R.id.tv_history);
		tv_dial = (TextView) findViewById(R.id.tv_dial);

		content = (LinearLayout) findViewById(R.id.content);

		tv_contact.setOnClickListener(this);
		tv_history.setOnClickListener(this);
		tv_dial.setOnClickListener(this);

		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.content, new Contacts());
		ft.commit();

	}

	@Override
	public void onClick(View v) {
		ft = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_contact:
			ft.replace(R.id.content, new Contacts());
			break;

		case R.id.tv_history:
			ft.replace(R.id.content, new History());
			break;
		case R.id.tv_dial:
			ft.replace(R.id.content, new Dial());
			break;
		}
		ft.commit();
	}

}
