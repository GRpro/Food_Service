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
import com.dao.entities.Receiver;

public class ReceiverDAO extends DAO<Receiver> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private UserDAO userDao;
	private MessageDAO messageDao;
	private CashDAO<Integer, Receiver> cash;
	
	public ReceiverDAO(Connection connection, UserDAO userDao, MessageDAO messageDao) {
		this.connection = connection;
		this.userDao = userDao;
		this.messageDao = messageDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO receiver"
					+ "(message, user)"
					+ "VALUES(?,?)");
			this.update = connection.prepareStatement("UPDATE receiver SET "
				+ "message = ?,user = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM receiver WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,Receiver>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashSet<Receiver> get(Condition<Receiver> condition)
			throws SQLException {
		return cash.get(condition);
	}

	@Override
	public Receiver create(Receiver object) throws SQLException {
		create.setInt(1, object.getMessage().getId());
		create.setInt(2, object.getUser().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM receiver");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public Receiver update(Receiver object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getMessage().getId());
		update.setInt(2, object.getUser().getId());
		update.setInt(3, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(Receiver object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}

	@Override
	public Receiver getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<Receiver> getFromDatabase() throws SQLException {
		HashSet<Receiver> res = new HashSet<Receiver>();
		ResultSet rs = statement.executeQuery("SELECT * FROM receiver");	
		while (rs.next()) {
			Receiver r = new Receiver(messageDao.getById(rs.getInt(2)), 
					userDao.getById(rs.getInt(3)));
			r.setId(rs.getInt(1));
			res.add(r);
		}
		return res;
	} 
}
