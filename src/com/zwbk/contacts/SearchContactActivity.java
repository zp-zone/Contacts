package com.zwbk.contacts;

import java.util.ArrayList;
import java.util.List;

import com.zwbk.contacts.domain.Person;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class SearchContactActivity extends Activity {
	private Person person = new Person();
	private List<Person> persons = new ArrayList<Person>();
	private List<String> names = new ArrayList<String>();
	private String name = new String();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchcontact);

		/**
		 * 读取数据库中联系人的信息 将联系人的名字读取到String 数组中，实现AutoCompleteTextView
		 */
		ContentResolver resolver = this.getContentResolver();
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
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(dataCursor
							.getColumnIndex("data1"));

					String mimeType = dataCursor.getString(dataCursor
							.getColumnIndex("mimetype"));

					if (mimeType.equals("vnd.android.cursor.item/name")) {
						person.setName(data1);
						names.add(data1);
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

		/**
		 * 找到自动填充控件，然后设置适配器 最后在对该控件设置一个条目点击事件
		 */
		AutoCompleteTextView contactNames = (AutoCompleteTextView) this
				.findViewById(R.id.actv_searchcontact);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, names);
		contactNames.setAdapter(adapter);

		contactNames.setOnItemClickListener(new OnItemClickListener() {

			@Override
			/**
			 * 由与第二个参数是被点击条目的view类型，不含有getText()函数，转换为TextView
			 * 之后再获取被点击条目的信息(虽然EditView也有getText()方法，
			 * 但是系统无法在EditText与TextView之间转换)
			 */
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// name = ((EditText) view).getText().toString().trim();
				TextView tv = (TextView) view;
				name = tv.getText().toString().trim();
				for (Person tempPerson : persons) {
					if (tempPerson.getName().equals(name)) {
						//在dataCursor.close()之前person被置为空，内存被回收，此处需要重新申请空间
						person = new Person();
						person.setName(tempPerson.getName());
						person.setPhonenum(tempPerson.getPhonenum());
						person.setEmail(tempPerson.getEmail());
						System.out.println("person info 1:" + person.getName()
								+ person.getPhonenum() + person.getEmail());
					}
				}
				/**
				 * 点击相应的联系人之后，跳转到联系人详细信息的界面
				 * 代码与Contacts中的条目点击事件；添加联系人的添加完成按钮的点击事件
				 * 的跳转页面完全相同，意图也是一样的
				 * 
				 * 跳转之后，本页面finish()掉
				 */
				Intent intent = new Intent(getApplicationContext(),
						SingleItemActivity.class);
				intent.putExtra("name", person.getName());
				intent.putExtra("phonenum", person.getPhonenum());
				intent.putExtra("email", person.getEmail());
				startActivity(intent);
				
				finish();
			}
		});
	}

}
