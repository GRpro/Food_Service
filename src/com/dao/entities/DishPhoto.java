package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class DishPhoto extends AbstractEntity<Integer> {
	
	private Photo photo;
	private Dish dish;
	
	public DishPhoto(Photo photo, Dish dish) {
		super();
		setId(-1);
		this.photo = photo;
		this.dish = dish;
	}

	public Photo getPhoto() {
		return photo;
	}

	public Dish getDish() {
		return dish;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	@Override
	public String toString() {
		return "DishPhoto [photo=" + photo + ", dish=" + dish + "]";
	}
	
	
}
