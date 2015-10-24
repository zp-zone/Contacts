package com.zwbk.contacts;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zwbk.contacts.domain.CallLog;

public class History extends Fragment {
	private ListView lv_history;
	private Button bt_clear;
	private CallLog callLog;// /系统也含有CallLog类
	private List<CallLog> callLogs = new ArrayList<CallLog>();
	private MyAdapter myAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.history, null);
		lv_history = (ListView) view.findViewById(R.id.lv_history);

		lv_history.setDividerHeight(0);

		bt_clear = (Button) view.findViewById(R.id.bt_clear);

		myAdapter = new MyAdapter();
		// /**
		// * 由于Contacts 中的MyAdapter设置的是private类型，所以这里访问不到
		// 但是如果改为public是否可以共用适配器？？
		// */
		// final MyAdapter myAdapter = new MyAdapter();
		// lv_history.setAdapter(myAdapter);
		//
		// ContentResolver resolver = getActivity().getContentResolver();
		// /**
		// * 和Contacts中的Uri的获取方式相同的情况下，未成功读取数据库
		// */
		// // Uri callsUri = Uri
		// // .parse("content://com.android.contacts/calls");
		//
		// Uri callsUri = android.provider.CallLog.Calls.CONTENT_URI;
		// Cursor cursor = resolver.query(callsUri, null, null, null, null);
		// while (cursor.moveToNext()) {
		// callLog = new CallLog();
		// String name = cursor.getString(cursor.getColumnIndex("name"));
		// String type = cursor.getString(cursor.getColumnIndex("type"));
		// String number = cursor.getString(cursor.getColumnIndex("number"));
		//
		// callLog.setName(name);
		// callLog.setPhonenum(number);
		// callLog.setType(type);
		//
		// callLogs.add(callLog);
		// }
		// cursor.close();
		//

		/**
		 * 设置清除联系人按钮的点击事件 三个步骤：新建builder；设置样式和内容；show()出来 相应的对话框上的按钮也需要设置点击事件
		 * 
		 * 对于fragment中的按钮点击事件使用 的是onVlickListener 但是对与builder新建的对话框的按钮点击事件
		 * 使用的也是onClickListener 貌似会产生冲突;冲突原因是导包的问题
		 */

		bt_clear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("确定清除历史记录");

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Uri deleteCallsUri = android.provider.CallLog.Calls.CONTENT_URI;
								ContentResolver deleteCallsResolver = getActivity()
										.getContentResolver();
								Cursor deleteCallsCursor = deleteCallsResolver
										.query(deleteCallsUri, null, null,
												null, null);
								while (deleteCallsCursor.moveToNext()) {
									String number = deleteCallsCursor
											.getString(deleteCallsCursor
													.getColumnIndex("number"));

									deleteCallsResolver
											.delete(deleteCallsUri, "number=?",
													new String[] { number });

								}
								callLogs.clear();
								// myAdapter.notifyDataSetChanged();
								lv_history.setAdapter(myAdapter);
								Toast.makeText(getActivity(), "删除通讯记录成功", 0)
										.show();
								deleteCallsCursor.close();
							}

						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				builder.show();
			}

		});

		return view;

	}

	/**
	 * 但获取到焦点之后，清空容器中的原有数据，再重新读取数据库并显示
	 */
	@Override
	public void onResume() {
		super.onResume();

		/**
		 * 由于Contacts 中的MyAdapter设置的是private类型，所以这里访问不到 但是如果改为public是否可以共用适配器？？
		 */

		lv_history.setAdapter(myAdapter);
		ContentResolver resolver = getActivity().getContentResolver();
		callLogs.clear();
		/**
		 * 和Contacts中的Uri的获取方式相同的情况下，未成功读取数据库
		 */
		// Uri callsUri = Uri
		// .parse("content://com.android.contacts/calls");

		Uri callsUri = android.provider.CallLog.Calls.CONTENT_URI;
		Cursor cursor = resolver.query(callsUri, null, null, null, null);
		while (cursor.moveToNext()) {
			callLog = new CallLog();
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String number = cursor.getString(cursor.getColumnIndex("number"));

			callLog.setName(name);
			callLog.setPhonenum(number);
			callLog.setType(type);

			callLogs.add(callLog);
		}
		cursor.close();

	}

	/**
	 * 自定义的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return callLogs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View
					.inflate(getActivity(), R.layout.history_item, null);

			TextView tv_name = (TextView) view
					.findViewById(R.id.tv_history_item_name);
			String name = callLogs.get(position).getName();
			if (name != null)
				tv_name.setText(name);

			TextView tv_phonenum = (TextView) view
					.findViewById(R.id.tv_history_item_phonenum);
			tv_phonenum.setText(callLogs.get(position).getPhonenum());

			TextView tv_type = (TextView) view
					.findViewById(R.id.tv_history_item_type);
			//String type = callLogs.get(position).getType();
			String type = callLogs.get(position).getType();
			if("1".equals(type)){
				tv_type.setText("接入");
			}else if("2".equals(type)){
				tv_type.setText("拨出");
			}else if("3".equals(type)){
				tv_type.setText("未接");
				tv_type.setBackgroundColor(1);
			}
			// Switch 使用String进行判断？？？？
//			switch (type) {
//			case "1": {
//				tv_type.setText("接入");
//				break;
//			}
//			case "2": {
//				tv_type.setText("拨出");
//				break;
//			}
//			case "3": {
//				tv_type.setText("未接");
//				tv_type.setBackgroundColor(1);
//				break;
//			}
//			}

			return view;
		}

	}
}
