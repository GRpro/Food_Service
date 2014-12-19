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
import com.dao.entities.Photo;
import com.dao.entities.User;

public class PhotoDAO extends DAO<Photo> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private CashDAO<Integer, Photo> cash;
	
	public PhotoDAO(Connection connection) {
		super();
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO photo"
				+ "(url)"
				+ "VALUES(?)");
			this.update = connection.prepareStatement("UPDATE photo SET "
				+ "url = ?"
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM photo WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,Photo>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public HashSet<Photo> get(Condition<Photo> condition) throws SQLException {
		return cash.get(condition);
	}
	@Override
	public Photo create(Photo object) throws SQLException {
		//create in database
		create.setString(1, object.getUrl());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM photo");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	@Override
	public Photo update(Photo object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getUrl());
		update.setInt(2, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	@Override
	public boolean delete(Photo object) throws SQLException {
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
	public Photo getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Photo> getFromDatabase() throws SQLException {
		HashSet<Photo> photos = new HashSet<Photo>();
		ResultSet rs = statement.executeQuery("SELECT * FROM photo");	
		while (rs.next()) {
			Photo p = new Photo(rs.getString(2));
			p.setId(rs.getInt(1));
			photos.add(p);
		}
		return photos;
	} 
	
}
