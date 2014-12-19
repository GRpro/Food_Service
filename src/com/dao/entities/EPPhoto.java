package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class EPPhoto extends AbstractEntity<Integer> {
	
	private Photo photo;
	private EP ep;
	
	public EPPhoto(Photo photo, EP ep) {
		super();
		setId(-1);
		this.photo = photo;
		this.ep = ep;
	}

	public Photo getPhoto() {
		return photo;
	}

	public EP getEp() {
		return ep;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public void setEp(EP ep) {
		this.ep = ep;
	}

	@Override
	public String toString() {
		return "EPPhoto [photo=" + photo + ", ep=" + ep + "]";
	}
	
	
}
