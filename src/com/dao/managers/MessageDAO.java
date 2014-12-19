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
import com.dao.entities.Message;
import com.dao.entities.Photo;
import com.dao.entities.Table;
import com.dao.entities.User;

public class MessageDAO extends DAO<Message> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private CashDAO<Integer, Message> cash;
	private UserDAO userDao;
	
	public MessageDAO(Connection connection, UserDAO userDao) {
		super();
		this.connection = connection;
		this.userDao = userDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO message"
					+ "(text, message, user, type)"
					+ "VALUES(?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE message SET "
				+ "text = ?, message = ?, user = ?, type = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM message WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,Message>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public HashSet<Message> get(Condition<Message> condition)
			throws SQLException {
		return cash.get(condition);
	}
	@Override
	public Message create(Message object) throws SQLException {
		//create in database
		create.setString(1, object.getText());
		if (object.getMessage() == null) {
			create.setNull(2, Types.INTEGER);
		} else {
			create.setInt(2, object.getMessage().getId());
		}
		create.setInt(3, object.getUser().getId());
		create.setInt(4, object.getType());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM message");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	@Override
	public Message update(Message object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getText());
		if (object.getMessage() == null) {
			update.setNull(2, Types.INTEGER);
		} else {
			update.setInt(2, object.getMessage().getId());
		}
		update.setInt(3, object.getUser().getId());
		update.setInt(4, object.getType());
		update.setInt(5, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	@Override
	public boolean delete(Message object) throws SQLException {
		//delete from database
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
//		if (delete.executeUpdate() != 1)
//			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	@Override
	public Message getById(Object id) throws SQLException {
		return cash.getById(id);	
	}
	@Override
	protected HashSet<Message> getFromDatabase() throws SQLException {
		HashSet<Message> messages = new HashSet<Message>();
		ResultSet rs = statement.executeQuery("SELECT * FROM message");	
		while (rs.next()) {
			
			Message m = new Message(rs.getString(2), userDao.getById(rs.getInt(4)), null, rs.getInt(5));
			m.setId(rs.getInt(1));
			messages.add(m);
		}
		rs.first();
		while (rs.next()) {
			if (rs.getObject(3) != null)
				getById(rs.getInt(1)).setMessage(getById(rs.getInt(3)));
		}
		return messages;
	} 
	
}
