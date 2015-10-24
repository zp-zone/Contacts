package com.zwbk.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zwbk.contacts.domain.Person;

public class SingleItemActivity extends Activity {

	private ListView lv_singleitem_contact;
	private Button bt_call;
	private Button bt_sendSMS;
	private Button bt_delete;

	private String ID;

	private Person person = new Person();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singleitem_activity);

		lv_singleitem_contact = (ListView) findViewById(R.id.lv_singleitem_contact);
		bt_call = (Button) findViewById(R.id.bt_call);
		bt_sendSMS = (Button) findViewById(R.id.bt_sendSMS);
		bt_delete = (Button) findViewById(R.id.bt_delete);

		lv_singleitem_contact.setDividerHeight(0);

		// //ListView的填充适配器，填充显示ListView中的内容
		MyAdapter adapter = new MyAdapter();
		lv_singleitem_contact.setAdapter(adapter);
		// 新建一个意图，接收从AddActivity传递过来的数据
		Intent intent = getIntent();
		person.setName(intent.getStringExtra("name"));
		person.setPhonenum(intent.getStringExtra("phonenum"));
		person.setEmail(intent.getStringExtra("email"));

		ID = intent.getStringExtra("id");

		/**
		 * 为三个按钮添加点击事件 由于是在activity中的点击事件，所以使用id添加监听事件，或者直接对每个按钮定义点击事件也可以
		 * com.android.contacts/.activities.DialtactsActivity
		 */
		bt_call.setOnClickListener(new OnClickListener() {

			/**
			 * 对于调用拨打电话的界面，需要设置action和data action是固定的，data的设置格式也是固定的
			 * 最后对于拨打电话的功能还需要在清单文件中配置权限
			 * 
			 * 注释部分新建了一个intent，使用findViewById的方式获取数据,这样会存在空指针的异常
			 * 解决办法是使用两个意图，一个用于获取数据intentData一个用于开启拨打电话的界面intentCall
			 */
			@Override
			public void onClick(View v) {
				Intent intentData = getIntent();
				String phonenum = intentData.getStringExtra("phonenum");
				// Intent intent = new Intent();
				// String phonenum = ((TextView) v
				// .findViewById(R.id.tv_singleitem_phonenum)).getText()
				// .toString().trim();

				Intent intentCall = new Intent();
				intentCall.setAction(Intent.ACTION_CALL);// 设置动作为拨打电话
				intentCall.setData(Uri.parse("tel:" + phonenum));// 对phonenum进行呼叫

				startActivity(intentCall);

			}
		});
		bt_sendSMS.setOnClickListener(new OnClickListener() {

			/*
			 * 调用系统的短信发送界面，并将得到的号码填入收信人的地址框中 (non-Javadoc)
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				Intent intentSendSMS = new Intent(Intent.ACTION_SENDTO, Uri
						.parse("smsto:" + person.getName()));
				startActivity(intentSendSMS);

			}
		});

		// bt_delete.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// AlertDialog.Builder builder = new Builder(
		// getApplicationContext());
		// builder.setTitle("删除联系人");
		// // builder.setPositiveButton("确定",
		// // new DialogInterface.OnClickListener() {
		// //
		// // @Override
		// // public void onClick(DialogInterface dialog,
		// // int which) {
		// // Toast.makeText(getApplicationContext(),
		// // "成功删除联系人", 0).show();
		// //
		// // }
		// // });
		// // builder.setNegativeButton("取消",
		// // new DialogInterface.OnClickListener() {
		// //
		// // @Override
		// // public void onClick(DialogInterface dialog,
		// // int which) {
		// // // TODO Auto-generated method stub
		// //
		// // }
		// // });
		// builder.show();
		//
		// }
		// });

	}

	// //这个地方使用ListView填充单个条目显然实现起来太麻烦
	// //但是没有找到既可以实现单个条目又可以实现条目点击响应的控件
	private class MyAdapter extends BaseAdapter {

		/**
		 * 由于这里是单个条目的填充，不存在容器，自然也就无法获知容器的大小 因此对于这个固定数目的填充，直接返回需要填充的数目
		 */
		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(getApplicationContext(),
					R.layout.singleitem_item, null);
			/**
			 * 将从intent中得到的数据填充到ListView中的其他view控件中
			 * 其中singleitem_item是inflate创建的一个view对象
			 */
			((TextView) view.findViewById(R.id.tv_singleitem_name))
					.setText(person.getName());
			((TextView) view.findViewById(R.id.tv_singleitem_phonenum))
					.setText(person.getPhonenum());
			((TextView) view.findViewById(R.id.tv_singleitem_email))
					.setText(person.getEmail());

			// TextView tv_phonenum = (TextView) view
			// .findViewById(R.id.tv_singleitem_phonenum);
			// tv_phonenum.setText(person.getPhonenum());
			//
			// TextView tv_email = (TextView) view
			// .findViewById(R.id.tv_singleitem_email);
			// tv_email.setText(person.getEmail());

			return view;
		}

	}

	public void deleteItem(View view) {
		/**
		 * 导致报这个错是在于new
		 * AlertDialog.Builder(mcontext)，虽然这里的参数是AlertDialog.Builder(Context
		 * context)但我们不能使用getApplicationContext()获得的Context,而必须使用Activity,
		 * 因为只有一个Activity才能添加一个窗体。 当前的activity直接使用的是this
		 */
		// AlertDialog.Builder builder = new Builder(getApplicationContext());
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("删除联系人信息");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ContentResolver resolver = getContentResolver();
				Uri rawContactsUri = Uri
						.parse("content://com.android.contacts/raw_contacts");

				Cursor cursor = resolver.query(rawContactsUri, null, null,
						null, null);
				while (cursor.moveToNext()) {
					String id = cursor.getString(cursor
							.getColumnIndex("contact_id"));
					if (id != null) {
						if (id.equals(ID))
							resolver.delete(rawContactsUri, "contact_id = ?",
									new String[] { ID });
					}
				}
				cursor.close();
				Toast.makeText(getApplicationContext(), "删除联系人信息", 0).show();
				/**
				 * 使用finish()函数将当前的activity关闭掉，回到点击之前的界面
				 * （或者为fragment从Contacts中点击ListView的条目的时候
				 * 或者为activity在添加联系人之后，不过不太可能刚添加就删除）
				 * 
				 * 在此还存在一个回到Contacts 的fragment的ListView的数据刷新的问题
				 */
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		builder.show();

	}
}
