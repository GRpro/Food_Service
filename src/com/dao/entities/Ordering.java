package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Ordering extends AbstractEntity<Integer> {
	
	private String serviceDate;
	private String deleiverDate;
	private boolean state;
	private User user;
	
	public Ordering(String serviceDate, String deleiverDate, boolean state,
			User user) {
		super();
		setId(-1);
		this.serviceDate = serviceDate;
		this.deleiverDate = deleiverDate;
		this.state = state;
		this.user = user;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public String getDeleiverDate() {
		return deleiverDate;
	}

	public boolean isState() {
		return state;
	}

	public User getUser() {
		return user;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public void setDeleiverDate(String deleiverDate) {
		this.deleiverDate = deleiverDate;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Ordering [serviceDate=" + serviceDate + ", deleiverDate="
				+ deleiverDate + ", state=" + state + ", user=" + user + "]";
	}
	
	
}
