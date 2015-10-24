package com.zwbk.contacts.domain;

import java.io.Serializable;

public class Person implements Serializable {

	/**
	 * 默认生成的ID，为了使意图可以传递自定义的person 对象，需要使Person 实现可序列化的接口
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String phonenum;
	private String email;

	@Override
	public String toString() {

		return "person [id =" + id + ",name = " + name + ",phonenum = "
				+ phonenum + "email" + email + "]";
	}

	public Person(String id, String name, String phonenum, String email) {
		super();
		this.id = id;
		this.name = name;
		this.phonenum = phonenum;
		this.email = email;
	}

	public Person() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
