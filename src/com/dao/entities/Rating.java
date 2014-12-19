package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Rating extends AbstractEntity<Integer> {
	
	private byte value;
	private String date;
	private User user;
	private EP ep;
	private String text;
	private boolean state;
	
	
	public Rating(byte value, String date, User user, EP ep, String text, boolean state) {
		super();
		setId(-1);
		this.value = value;
		this.date = date;
		this.user = user;
		this.ep = ep;
		this.text = text;
		this.state = state;
	}
	
	public byte getValue() {
		return value;
	}
	public String getDate() {
		return date;
	}
	public User getUser() {
		return user;
	}
	public EP getEp() {
		return ep;
	}
	public String getText() {
		return text;
	}
	public boolean isState() {
		return state;
	}
	public void setValue(byte value) {
		this.value = value;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setEp(EP ep) {
		this.ep = ep;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Rating [value=" + value + ", date=" + date + ", user=" + user
				+ ", ep=" + ep + ", text=" + text + ", state=" + state + "]";
	}
	
	
}
