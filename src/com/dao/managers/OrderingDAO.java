package com.dao.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import com.dao.abstractions.Condition;
import com.dao.abstractions.DAO;
import com.dao.cash.CashDAO;
import com.dao.entities.Ordering;
import com.dao.entities.Rating;

public class OrderingDAO extends DAO<Ordering> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private UserDAO userDao;
	private CashDAO<Integer, Ordering> cash;
	
	public OrderingDAO(Connection connection, UserDAO userDao) {
		super();
		this.connection = connection;
		this.userDao = userDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO ordering"
					+ "(serviceDate, deliverDate, state, user)"
					+ "VALUES(?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE ordering SET "
				+ "serviceDate = ? , deliverDate = ?, state = ?, user = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM ordering WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer, Ordering>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<Ordering> get(Condition<Ordering> condition)
			throws SQLException {
		return cash.get(condition);
	}
	
	@Override
	public Ordering create(Ordering object) throws SQLException {
		create.setString(1, object.getServiceDate());
		create.setString(2, object.getDeleiverDate());
		create.setBoolean(3, object.isState());
		create.setInt(4, object.getUser().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM ordering");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	@Override
	public Ordering update(Ordering object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getServiceDate());
		update.setString(2, object.getDeleiverDate());
		update.setBoolean(3, object.isState());
		update.setInt(4, object.getUser().getId());
		update.setInt(5, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Ordering object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public Ordering getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Ordering> getFromDatabase() throws SQLException {
		HashSet<Ordering> orderings = new HashSet<Ordering>();
		ResultSet rs = statement.executeQuery("SELECT * FROM ordering");	
		while (rs.next()) {
			Ordering o =  new Ordering(rs.getString(2), rs.getString(3), 
					rs.getBoolean(4), userDao.getById(rs.getInt(5)));
			o.setId(rs.getInt(1));
			orderings.add(o);
		}
		return orderings;
	} 
	
}
