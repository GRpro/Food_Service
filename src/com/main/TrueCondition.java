package com.main;

import com.dao.abstractions.Condition;

public class TrueCondition<T> implements Condition<T> {

	@Override
	public boolean satisfyTo(T object) {
		return true;
	}

}
