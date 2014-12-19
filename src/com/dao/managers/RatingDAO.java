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
import com.dao.entities.Rating;
import com.dao.entities.User;

public class RatingDAO extends DAO<Rating> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private UserDAO userDao;
	private EPDAO epDao;
	private CashDAO<Integer, Rating> cash;
	
	public RatingDAO(Connection connection, UserDAO userDao, EPDAO epDao) {
		super();
		this.connection = connection;
		this.userDao = userDao;
		this.epDao = epDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO rating"
					+ "(value, date, user, ep, text, state)"
					+ "VALUES(?,?,?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE rating SET "
				+ "value = ?,date = ?,user = ?,ep = ?,text = ?,state = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM rating WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer, Rating>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<Rating> get(Condition<Rating> condition) throws SQLException {
		return cash.get(condition);
	}
	
	@Override
	public Rating create(Rating object) throws SQLException {
		create.setInt(1, object.getValue());
		create.setString(2, object.getDate());
		create.setInt(3, object.getUser().getId());
		create.setInt(4, object.getEp().getId());
		create.setString(5, object.getText());
		create.setBoolean(6, object.isState());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM rating");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	@Override
	public Rating update(Rating object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getValue());
		update.setString(2, object.getDate());
		update.setInt(3, object.getUser().getId());;
		update.setInt(4, object.getEp().getId());
		update.setString(5, object.getText());
		update.setBoolean(6, object.isState());
		update.setInt(7, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Rating object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public Rating getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Rating> getFromDatabase() throws SQLException {
		HashSet<Rating> rate = new HashSet<Rating>();
		ResultSet rs = statement.executeQuery("SELECT * FROM rating");	
		while (rs.next()) {
			Rating r = new Rating(rs.getByte(2), rs.getString(3), userDao.getById(rs.getInt(4)),
					epDao.getById(rs.getInt(5)), rs.getString(6), rs.getBoolean(7));
			r.setId(rs.getInt(1));
			rate.add(r);
		}
		return rate;
	} 
	
}
