package com.dao.cash;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import com.dao.abstractions.Condition;
import com.dao.abstractions.DAO;
import com.dao.managers.AbstractEntity;



/**
 * Cash class which allows increase performance of DAO pattern by 
 * getting SELECT information from this cash without connection to the database
 * @author Grigoriy
 *
 * @param <K> special types of keys
 * @param <V> special types of cash values
 */
public class CashDAO<K, V extends AbstractEntity<K>> extends DAO<V> {
	
	private  HashMap<K, V> cash;
	
	public CashDAO(HashSet<V> cash) {
		this.cash = new HashMap<K, V>();
		cash.forEach(v -> this.cash.put(v.getId(), v));
	}

	@Override
	public HashSet<V> get(Condition<V> condition) {
		HashSet<V> hs = new HashSet<>();
		for ( V entity : cash.values())
			if(condition.satisfyTo(entity))
				hs.add(entity);
		return hs;
	}

	@Override
	public V create(V object) throws SQLException {
		this.cash.put(object.getId(), object);
		return object;
	}

	@Override
	public V update(V object) throws SQLException {
		this.cash.replace(object.getId(), this.cash.get(object.getId()), object);
		return object;
	}

	@Override
	public boolean delete(V object) throws SQLException {
		if(this.cash.remove(object.getId(), object) == false)
			throw new SQLException();
		return true;
	}

	@Override
	public V getById(Object id) throws SQLException {
		return this.cash.get(id);
	}

	@Override
	protected HashSet<V> getFromDatabase() throws SQLException {
		throw new UnsupportedOperationException();
	}
}
