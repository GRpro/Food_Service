package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Discount extends AbstractEntity<Integer> {
	
	private String startTime;
	private String endTime;
	private float persentage;
	private Dish dish;
	
	
	public Discount(String startTime, String endTime, float persentage,
			Dish dish) {
		super();
		setId(-1);
		this.startTime = startTime;
		this.endTime = endTime;
		this.persentage = persentage;
		this.dish = dish;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public float getPersentage() {
		return persentage;
	}
	public Dish getDish() {
		return dish;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void setPersentage(float persentage) {
		this.persentage = persentage;
	}
	public void setDish(Dish dish) {
		this.dish = dish;
	}

	@Override
	public String toString() {
		return "Discount [startTime=" + startTime + ", endTime=" + endTime
				+ ", persentage=" + persentage + ", dish=" + dish + "]";
	}
}
