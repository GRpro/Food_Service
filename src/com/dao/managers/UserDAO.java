package com.dao.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;

import com.dao.abstractions.Condition;
import com.dao.abstractions.DAO;
import com.dao.cash.CashDAO;
import com.dao.entities.User;


public class UserDAO extends DAO<User>{

	private Connection connection;
	private Statement statement; 
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private CashDAO<Integer, User> cash;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO user"
					+ "(firstName, lastName, login, password, type, sex, personal_data)"
					+ "VALUES(?,?,?,?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE user SET "
				+ "firstName = ?,lastName = ?,login = ?,password = ?,"
				+ "type = ?,sex = ?,personal_data = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM user WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,User>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected HashSet<User> getFromDatabase() throws SQLException {
		HashSet<User> users = new HashSet<User>();
		ResultSet rs = statement.executeQuery("SELECT * FROM user");	
		while (rs.next()) {
			User u = new User(rs.getString(2), rs.getString(3), 
					rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8));
			u.setId(rs.getInt(1));
			users.add(u);
		}
		return users;
	}

	@Override
	public User create(User object) throws SQLException {
		//create in database
		create.setString(1, object.getFirstName());
		create.setString(2, object.getLastName());
		create.setString(3, object.getLogin());
		create.setString(4, object.getPassword());
		create.setInt(5, object.getType());
		create.setString(6, object.getSex());
		if (object.getPersonalData() == null)
			create.setNull(7, Types.VARCHAR);
		else 
			create.setString(7, object.getPersonalData());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM user");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public User update(User object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getFirstName());
		update.setString(2, object.getLastName());
		update.setString(3, object.getLogin());
		update.setString(4, object.getPassword());
		update.setInt(5, object.getType());
		update.setString(6, object.getSex());
		if (object.getPersonalData() == null) {
			update.setNull(7, Types.VARCHAR);
		} else 
			update.setString(7, object.getPersonalData());
		update.setInt(8, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(User object) throws SQLException {
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
	public User getById(Object id) throws SQLException {
		return cash.getById(id);	
	}

	@Override
	public HashSet<User> get(Condition<User> condition) {
		return cash.get(condition);
	}
}
