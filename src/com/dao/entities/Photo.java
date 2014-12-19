package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Photo extends AbstractEntity<Integer> {
	
	private String url;

	public Photo(String url) {
		super();
		setId(-1);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Photo [url=" + url + "]";
	}
}
