package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class User extends AbstractEntity<Integer> {
	
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	private int type;
	private String sex;
	private String personalData;
	
	
	public User(String firstName, String lastName, String login,
			String password, int type, String sex, String personalData) {
		super();
		setId(-1);
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.type = type;
		this.sex = sex;
		this.personalData = personalData;
	}


	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPersonalData() {
		return personalData;
	}
	public void setPersonalData(String personalData) {
		this.personalData = personalData;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", login=" + login + ", password=" + password + ", type="
				+ type + ", sex=" + sex + ", personalData=" + personalData
				+ "]";
	}

}
