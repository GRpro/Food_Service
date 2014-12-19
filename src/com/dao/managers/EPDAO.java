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
import com.dao.entities.EP;

public class EPDAO extends DAO<EP>{
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private CashDAO<Integer, EP> cash;
	
	public EPDAO(Connection connection) {
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO ep"
				+ "(name, address, phone) VALUES(?,?,?)");
			this.update = connection.prepareStatement("UPDATE ep SET "
					+ "name = ?, address = ?, phone = ? WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM ep WHERE id = ?");
			//load into cash
			cash = new CashDAO<Integer, EP>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public HashSet<EP> get(Condition<EP> condition) throws SQLException {
		return cash.get(condition);
	}
	@Override
	public EP create(EP object) throws SQLException {
		create.setString(1, object.getName());
		create.setString(2, object.getAddress());
		create.setString(3, object.getPhone());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM ep");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	@Override
	public EP update(EP object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getName());
		update.setString(2, object.getAddress());
		update.setString(3, object.getPhone());
		update.setInt(4, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	@Override
	public boolean delete(EP object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	@Override
	public EP getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	@Override
	protected HashSet<EP> getFromDatabase() throws SQLException {
		HashSet<EP> eps = new HashSet<EP>();
		ResultSet rs = statement.executeQuery("SELECT * FROM ep");	
		while (rs.next()) {
			EP ep = new EP(rs.getString(2), rs.getString(3), rs.getString(4));
			ep.setId(rs.getInt(1));
			eps.add(ep);
		}
		return eps;
	} 
	
	
}
