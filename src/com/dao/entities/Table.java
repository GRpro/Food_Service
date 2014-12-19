package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Table extends AbstractEntity<Integer> {
	
	private int number;
	private int size;
	private EP ep;
	
	
	public Table(int number, int size, EP ep) {
		super();
		setId(-1);
		this.number = number;
		this.size = size;
		this.ep = ep;
	}
	
	public int getNumber() {
		return number;
	}
	public int getSize() {
		return size;
	}
	public EP getEp() {
		return ep;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setEp(EP ep) {
		this.ep = ep;
	}

	@Override
	public String toString() {
		return "Table [number=" + number + ", size=" + size + ", ep=" + ep
				+ "]";
	}

}
