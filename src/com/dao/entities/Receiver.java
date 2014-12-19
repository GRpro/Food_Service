package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Receiver extends AbstractEntity<Integer> {
	
	private Message message;
	private User user;
	
	public Receiver(Message message, User user) {
		super();
		setId(-1);
		this.message = message;
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Receiver [message=" + message + ", user=" + user + "]";
	}
	
	
}
