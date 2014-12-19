package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Friends extends AbstractEntity<Integer> {
	
	private User user1;
	private User user2;
	
	
	public Friends(User user1, User user2) {
		super();
		setId(-1);
		this.user1 = user1;
		this.user2 = user2;
	}
	
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@Override
	public String toString() {
		return "Friends [user1=" + user1 + ", user2=" + user2 + "]";
	}

}
