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
import com.dao.entities.Friends;
import com.dao.entities.User;

public class FriendsDAO extends DAO<Friends>{
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private UserDAO userDao;
	private CashDAO<Integer, Friends> cash;
	
	public FriendsDAO(Connection connection, UserDAO userDao) {
		this.connection = connection;
		this.userDao = userDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO friends"
				+ "(user1, user2) VALUES(?,?)");
			this.update = connection.prepareStatement("UPDATE friends SET "
					+ "user1 = ?,user2 = ? WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM friends WHERE id = ?");
			//load into cash
			cash = new CashDAO<>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public Friends create(Friends object) throws SQLException {
		//create in database
		create.setInt(1, object.getUser1().getId());
		create.setInt(2, object.getUser2().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM friends");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	
	@Override
	public Friends update(Friends object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getUser1().getId());
		update.setInt(2, object.getUser2().getId());
		update.setInt(3, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Friends object) throws SQLException {
		//delete from database
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public HashSet<Friends> get(Condition<Friends> condition)
			throws SQLException {
		return cash.get(condition);
	}
	
	
	@Override
	public Friends getById(Object id) throws SQLException {
		return cash.getById(id);
	}


	@Override
	protected HashSet<Friends> getFromDatabase() throws SQLException {
		HashSet<Friends> friends = new HashSet<Friends>();
		ResultSet rs = statement.executeQuery("SELECT * FROM friends");	
		while (rs.next()) {
			Friends f = new Friends(userDao.getById(rs.getInt(2)), 
									userDao.getById(rs.getInt(3)));
			f.setId(rs.getInt(1));
			friends.add(f);
		}
		return friends;
	} 
	
}
