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
import com.dao.entities.EPPhoto;


public class EPPhotoDAO extends DAO<EPPhoto> {

	private Connection connection;
	private Statement statement; 
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private EPDAO epDao;
	private PhotoDAO photoDao;
	private CashDAO<Integer, EPPhoto> cash;
	
	public EPPhotoDAO(Connection connection, EPDAO epDao, PhotoDAO photoDao) {
		this.connection = connection;
		this.epDao = epDao;
		this.photoDao = photoDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO epphoto"
					+ "(photo, dish)"
					+ "VALUES(?,?)");
			this.update = connection.prepareStatement("UPDATE epphoto SET "
				+ "photo = ?,dish = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM epphoto WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,EPPhoto>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<EPPhoto> get(Condition<EPPhoto> condition)
			throws SQLException {
		return cash.get(condition);
	}

	@Override
	public EPPhoto create(EPPhoto object) throws SQLException {
		create.setInt(1, object.getPhoto().getId());
		create.setInt(2, object.getEp().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM epphoto");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public EPPhoto update(EPPhoto object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getPhoto().getId());
		update.setInt(2, object.getEp().getId());
		update.setInt(3, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(EPPhoto object) throws SQLException {
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
	public EPPhoto getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<EPPhoto> getFromDatabase() throws SQLException {
		HashSet<EPPhoto> epphotos = new HashSet<EPPhoto>();
		ResultSet rs = statement.executeQuery("SELECT * FROM epphoto");	
		while (rs.next()) {
			EPPhoto tp = new EPPhoto(photoDao.getById(rs.getInt(2)), 
					epDao.getById(rs.getInt(3)));
			tp.setId(rs.getInt(1));
			epphotos.add(tp);
		}
		return epphotos;
	}
}

