package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class EP extends AbstractEntity<Integer> {
	
	private String name;
	private String address;
	private String phone;
	
	
	public EP(String name, String address, String phone) {
		super();
		setId(-1);
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "EP [name=" + name + ", address=" + address + ", phone=" + phone
				+ "]";
	}
	
	
}
