package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Block extends AbstractEntity<Integer> {
	
	private Table table;
	private String startTime;
	private String endTime;
	
	public Block(Table table, String startTime, String endTime) {
		super();
		setId(-1);
		this.table = table;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Table getTable() {
		return table;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Block [table=" + table + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}
}
