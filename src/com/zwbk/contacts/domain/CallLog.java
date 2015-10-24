package com.zwbk.contacts.domain;

/**
 * 通讯记录类：三个成员变量分别是通讯人姓名电话和通讯类型
 * 
 * @author Administrator
 * 
 */
public class CallLog {
	private String type;
	private String name;
	private String phonenum;

	public CallLog(String type, String name, String phonenum) {
		super();
		this.type = type;
		this.name = name;
		this.phonenum = phonenum;
	}

	public CallLog() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

}
