package com.zwbk.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class Dial extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用意图调用系统拨号界面
		 */
		Intent intentDial = new Intent();
		intentDial.setAction(Intent.ACTION_DIAL);

		startActivity(intentDial);
	}

}
