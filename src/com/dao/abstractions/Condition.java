package com.dao.abstractions;

public interface Condition<T> {
	public boolean satisfyTo(T object);
}
