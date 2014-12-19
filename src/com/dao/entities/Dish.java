package com.dao.entities;

import com.dao.managers.AbstractEntity;

public class Dish extends AbstractEntity<Integer> {
	
	private String name;
	private String cuisine;
	private float price;
	private EP ep;
	
	public Dish(String name, String cuisine, float price, EP ep) {
		super();
		setId(-1);
		this.name = name;
		this.cuisine = cuisine;
		this.price = price;
		this.ep = ep;
	}
	
	public String getName() {
		return name;
	}
	public String getCuisine() {
		return cuisine;
	}
	public float getPrice() {
		return price;
	}
	public EP getEp() {
		return ep;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setEp(EP ep) {
		this.ep = ep;
	}

	@Override
	public String toString() {
		return "Dish [name=" + name + ", cuisine=" + cuisine + ", price="
				+ price + ", ep=" + ep + "]";
	}
}
