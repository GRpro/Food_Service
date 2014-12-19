package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Position extends AbstractEntity<Integer> {
	
	private Dish dish;
	private int number;
	private Ordering ordering;
	
	public Position(Dish dish, int number, Ordering ordering) {
		super();
		setId(-1);
		this.dish = dish;
		this.number = number;
		this.ordering = ordering;
	}

	public Dish getDish() {
		return dish;
	}

	public int getNumber() {
		return number;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	@Override
	public String toString() {
		return "Position [dish=" + dish + ", number=" + number + ", ordering="
				+ ordering + "]";
	}

	
}
