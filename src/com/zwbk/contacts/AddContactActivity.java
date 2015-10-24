package com.zwbk.contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity {

	private EditText et_name;
	private EditText et_phonenum;
	private EditText et_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addcontact);
		// 根据id找到三个控件
		et_name = (EditText) findViewById(R.id.et_name);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		et_email = (EditText) findViewById(R.id.et_email);

	}

	public void adddone(View view) {

		// 根据控件得到控件 中用户输入的内容
		String name = et_name.getText().toString().trim();
		String phonenum = et_phonenum.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		

		// ///if (name != null && phonenum != null) {//输入姓名和电话就可以完成添加,并未实现
		// ////使用contentResolver将得到的数据插入到数据库
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		// 在raw_contacts表中插入一个新的ID，然后下面根据这个新的ID在data表插入其他数据
		int newId;
		// //第二个参数为什么不是"_id"
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		cursor.moveToLast();
		newId = 1 + cursor.getInt(0);
		ContentValues idValues = new ContentValues();
		// 为什么这个地方使用的是_id进行查找，使用contact_id进行插入
		idValues.put("contact_id", newId);
		resolver.insert(uri, idValues);
		
		ContentValues nameValues = new ContentValues();
		nameValues.put("data1", name);
		nameValues.put("mimetype", "vnd.android.cursor.item/name");
		nameValues.put("raw_contact_id", newId);
		resolver.insert(dataUri, nameValues);

		ContentValues phonenumValues = new ContentValues();
		phonenumValues.put("data1", phonenum);
		phonenumValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
		phonenumValues.put("raw_contact_id", newId);
		resolver.insert(dataUri, phonenumValues);

		ContentValues emailValues = new ContentValues();
		emailValues.put("data1", email);
		emailValues.put("mimetype", "vnd.android.cursor.item/email_v2");
		emailValues.put("raw_contact_id", newId);
		resolver.insert(dataUri, emailValues);

		cursor.close();
		
		Toast.makeText(getApplicationContext(), "添加成功", 0).show();

		/**
		 * 由于Contacts不是activity所以使用意图实现页面跳转的方式不太可行
		 * 一个可行的办法是新建一个activity和相应的xml文件，该activity用于显示一条联系人信息
		 * 当listview的条目被点击之后跳转到该条目；当联系人存储完成之后也跳转到该条目
		 * 此处新建了一个SingleItem的activity实现跳转
		 */
		Intent intent = new Intent(getApplicationContext(),
				SingleItemActivity.class);
		/**
		 * 这里使用意图在addContactActivity与SingleItemActivity之间传递一个person对象的三个成员变量值
		 * intent的put函数可以put自定义类型吗？？此处需要传递的是person类型的一个对象
		 * 
		 * 在person实现了可序列化的接口之后，就可以实现传递自定义的对象了
		 */
		
		intent.putExtra("name", name);
		intent.putExtra("phonenum", phonenum);
		intent.putExtra("email", email);
		startActivity(intent);

		/**
		 * 所有的操作完成之后可以调用finish()函数将这个activity关闭
		 */
		finish();
		// }
		// else{
		// Toast.makeText(getApplicationContext(), "姓名和电话不能为空", 0).show();
		// }

	}
}
