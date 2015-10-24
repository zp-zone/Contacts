package com.zwbk.contacts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zwbk.contacts.domain.Person;

public class Contacts extends Fragment {

	private ListView lv_contact;
	public Person person;
	public List<Person> persons = new ArrayList<Person>();


	@Override
	public void onResume() {
		super.onResume();
		MyAdapter myAdapter = new MyAdapter();
		lv_contact.setAdapter(myAdapter);

		persons.clear();
		
		ContentResolver resolver = getActivity().getContentResolver();
		Uri rawContactsUri = Uri
				.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(rawContactsUri, null, null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("contact_id"));
			if (id != null) {
				person = new Person();
				person.setId(id);
				Cursor dataCursor = resolver.query(dataUri, null,
						"raw_contact_id = ?", new String[] { id }, null);

				// _id mimetype 1 vnd.android.cursor.item/email_v2
				// _id mimetype 7 vnd.android.cursor.item/name
				// _id mimetype 9 vnd.android.cursor.item/identity
				// _id mimetype 5 vnd.android.cursor.item/phone_v2

				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(dataCursor
							.getColumnIndex("data1"));

					String mimeType = dataCursor.getString(dataCursor
							.getColumnIndex("mimetype"));

					if (mimeType.equals("vnd.android.cursor.item/name")) {
						person.setName(data1);
					}
					if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
						person.setPhonenum(data1);
					}
					if (mimeType.equals("vnd.android.cursor.item/email_v2")) {
						person.setEmail(data1);
					}

				}
				persons.add(person);
				person = null;
				dataCursor.close();
			}

		}
		cursor.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.contact, null);
		lv_contact = (ListView) view.findViewById(R.id.lv_contact);

//		MyAdapter myAdapter = new MyAdapter();
//		lv_contact.setAdapter(myAdapter);
//
//		ContentResolver resolver = getActivity().getContentResolver();
//		Uri rawContactsUri = Uri
//				.parse("content://com.android.contacts/raw_contacts");
//		Uri dataUri = Uri.parse("content://com.android.contacts/data");
//
//		Cursor cursor = resolver.query(rawContactsUri, null, null, null, null);
//		while (cursor.moveToNext()) {
//			String id = cursor.getString(cursor.getColumnIndex("contact_id"));
//			if (id != null) {
//				person = new Person();
//				person.setId(id);
//				Cursor dataCursor = resolver.query(dataUri, null,
//						"raw_contact_id = ?", new String[] { id }, null);
//
//				// _id mimetype 1 vnd.android.cursor.item/email_v2
//				// _id mimetype 7 vnd.android.cursor.item/name
//				// _id mimetype 9 vnd.android.cursor.item/identity
//				// _id mimetype 5 vnd.android.cursor.item/phone_v2
//
//				while (dataCursor.moveToNext()) {
//					String data1 = dataCursor.getString(dataCursor
//							.getColumnIndex("data1"));
//
//					String mimeType = dataCursor.getString(dataCursor
//							.getColumnIndex("mimetype"));
//
//					if (mimeType.equals("vnd.android.cursor.item/name")) {
//						person.setName(data1);
//					}
//					if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
//						person.setPhonenum(data1);
//					}
//					if (mimeType.equals("vnd.android.cursor.item/email_v2")) {
//						person.setEmail(data1);
//					}
//
//				}
//				persons.add(person);
//				person = null;
//				dataCursor.close();
//			}
//
//		}
//		cursor.close();

		/**
		 * ListView的条目点击事件
		 */
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/**
				 * 条目被点击之后将被点击的条目的数据取出来，同过意图发送给另外一个activity
				 * 这里把意图开启之后，相应的也需要将需要传递的数据发送过去
				 * 通过参数view和ID将其中的三个TextView找到，获取其中的Text,再转化为String后菜地给局部变量
				 * 最后通过意图发送给其他的activity
				 */
				String ID = ((TextView) view.findViewById(R.id.tv_id))
						.getText().toString().trim();
				String name = ((TextView) view.findViewById(R.id.tv_name))
						.getText().toString().trim();
				String phonemun = ((TextView) view
						.findViewById(R.id.tv_phonenum)).getText().toString()
						.trim();
				String email = ((TextView) view.findViewById(R.id.tv_email))
						.getText().toString().trim();

				Intent intent = new Intent(getActivity(),
						SingleItemActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("phonenum", phonemun);
				intent.putExtra("email", email);
				intent.putExtra("id", ID);
				startActivity(intent);
			}
		});// end listener

		/*
		 * 两个按键的点击事件响应 要跳转的页面是activity所以直接使用意图就可以实现跳转
		 * 而如果想要从AddContactActivity跳转回来就属于是从activity向fragment跳转
		 */
		Button add = (Button) view.findViewById(R.id.bt_add);
		Button search = (Button) view.findViewById(R.id.bt_search);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						AddContactActivity.class);

				startActivity(intent);
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SearchContactActivity.class);
				startActivity(intent);
			}
		});

		
		return view;
	}// end onCreateView

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return persons.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return persons.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View
					.inflate(getActivity(), R.layout.conatct_item, null);

			TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
			tv_id.setText(persons.get(position).getId());

			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_name.setText(persons.get(position).getName());

			TextView tv_phonenum = (TextView) view
					.findViewById(R.id.tv_phonenum);
			tv_phonenum.setText(persons.get(position).getPhonenum());

			TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
			tv_email.setText(persons.get(position).getEmail());

			return view;
		}// end getView

	}// end MyAdapter

	// //对于fragment使用的是在createView里面的按键监听实现点击事件的响应
	// /**
	// * 添加联系人的点击事件
	// * @param view
	// */
	// public void add(View view) {
	//
	// Intent intent = new Intent(getActivity(),AddContactActivity.class);
	// startActivity(intent);
	// }
	//
	// /**
	// * 查找联系人的点击事件
	// * @param view
	// */
	// public void search(View view) {
	//
	// Intent intent = new Intent(getActivity(),SearchContactActivity.class);
	// startActivity(intent);
	// }
	
}
