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
import com.dao.entities.DishPhoto;


public class DishPhotoDAO extends DAO<DishPhoto> {

	private Connection connection;
	private Statement statement; 
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private DishDAO dishDao;
	private PhotoDAO photoDao;
	private CashDAO<Integer, DishPhoto> cash;
	
	public DishPhotoDAO(Connection connection, DishDAO dishDao, PhotoDAO photoDao) {
		this.connection = connection;
		this.dishDao = dishDao;
		this.photoDao = photoDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO dishphoto"
					+ "(photo, dish)"
					+ "VALUES(?,?)");
			this.update = connection.prepareStatement("UPDATE dishphoto SET "
				+ "photo = ?,dish = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM dishphoto WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,DishPhoto>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<DishPhoto> get(Condition<DishPhoto> condition)
			throws SQLException {
		return cash.get(condition);
	}

	@Override
	public DishPhoto create(DishPhoto object) throws SQLException {
		create.setInt(1, object.getPhoto().getId());
		create.setInt(2, object.getDish().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM dishphoto");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public DishPhoto update(DishPhoto object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getPhoto().getId());
		update.setInt(2, object.getDish().getId());
		update.setInt(3, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(DishPhoto object) throws SQLException {
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
	public DishPhoto getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<DishPhoto> getFromDatabase() throws SQLException {
		HashSet<DishPhoto> dishphotos = new HashSet<DishPhoto>();
		ResultSet rs = statement.executeQuery("SELECT * FROM dishphoto");	
		while (rs.next()) {
			DishPhoto tp = new DishPhoto(photoDao.getById(rs.getInt(2)), 
					dishDao.getById(rs.getInt(3)));
			tp.setId(rs.getInt(1));
			dishphotos.add(tp);
		}
		return dishphotos;
	}
}
