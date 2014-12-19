package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class TablePhoto extends AbstractEntity<Integer> {
	
	private Photo photo;
	private Table table;
	
	public TablePhoto(Photo photo, Table table) {
		super();
		setId(-1);
		this.photo = photo;
		this.table = table;
	}

	public Photo getPhoto() {
		return photo;
	}

	public Table getTable() {
		return table;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return "TablePhoto [photo=" + photo + ", table=" + table + "]";
	}
	
	
}