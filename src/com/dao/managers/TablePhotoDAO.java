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
import com.dao.entities.TablePhoto;

public class TablePhotoDAO extends DAO<TablePhoto> {

	private Connection connection;
	private Statement statement; 
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private TableDAO tableDao;
	private PhotoDAO photoDao;
	private CashDAO<Integer, TablePhoto> cash;
	
	public TablePhotoDAO(Connection connection, TableDAO tableDao, PhotoDAO photoDao) {
		this.connection = connection;
		this.tableDao = tableDao;
		this.photoDao = photoDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO tablephoto"
					+ "(photo, table)"
					+ "VALUES(?,?)");
			this.update = connection.prepareStatement("UPDATE tablephoto SET "
				+ "photo = ?,table = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM tablephoto WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer,TablePhoto>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<TablePhoto> get(Condition<TablePhoto> condition)
			throws SQLException {
		return cash.get(condition);
	}

	@Override
	public TablePhoto create(TablePhoto object) throws SQLException {
		create.setInt(1, object.getPhoto().getId());
		create.setInt(2, object.getTable().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM tablephoto");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public TablePhoto update(TablePhoto object) throws SQLException {
		//update in database
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getPhoto().getId());
		update.setInt(2, object.getTable().getId());
		update.setInt(3, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(TablePhoto object) throws SQLException {
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
	public TablePhoto getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<TablePhoto> getFromDatabase() throws SQLException {
		HashSet<TablePhoto> tablephotos = new HashSet<TablePhoto>();
		ResultSet rs = statement.executeQuery("SELECT * FROM tablephoto");	
		while (rs.next()) {
			TablePhoto tp = new TablePhoto(photoDao.getById(rs.getInt(2)), 
					tableDao.getById(rs.getInt(3)));
			tp.setId(rs.getInt(1));
			tablephotos.add(tp);
		}
		return tablephotos;
	}
}
