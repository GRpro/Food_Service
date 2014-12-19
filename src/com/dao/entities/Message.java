package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Message extends AbstractEntity<Integer> {
	private String text;
	private User user;
	private Message message;
	private int type;
	
	
	public Message(String text, User user, Message message, int type) {
		super();
		setId(-1);
		this.type = type;
		this.text = text;
		this.user = user;
		this.message = message;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Message [text=" + text + ", user=" + user + ", message="
				+ message + ", type=" + type + "]";
	}
	
}
