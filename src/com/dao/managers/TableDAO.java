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
import com.dao.entities.EP;
import com.dao.entities.Friends;
import com.dao.entities.Table;
import com.dao.entities.User;

public class TableDAO extends DAO<Table> {
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private EPDAO epDao;
	private CashDAO<Integer, Table> cash;
	
	public TableDAO(Connection connection,EPDAO epDao) {
		super();
		this.connection = connection;
		this.epDao = epDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO food_service.table "
				+ "(number, size, ep) VALUES(?,?,? )");
			this.update = connection.prepareStatement("UPDATE food_service.table SET "
					+ "number = ?, size = ?, ep = ? WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM food_service.table WHERE id = ?");
			//load into cash
			cash = new CashDAO<Integer, Table>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashSet<Table> get(Condition<Table> condition) throws SQLException {
		return cash.get(condition);
	}

	@Override
	public Table create(Table object) throws SQLException {
		//create in database
		create.setInt(1, object.getNumber());
		create.setInt(2, object.getSize());
		create.setInt(3, object.getEp().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM food_service.table");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public Table update(Table object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getNumber());
		update.setInt(2, object.getSize());
		update.setInt(3, object.getEp().getId());
		update.setInt(4, object.getId());
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(Table object) throws SQLException {
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
	public Table getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<Table> getFromDatabase() throws SQLException {
		HashSet<Table> tables = new HashSet<Table>();
		ResultSet rs = statement.executeQuery("SELECT * FROM food_service.table");	
		while (rs.next()) {
			Table t = new Table(rs.getInt(2), rs.getInt(3), epDao.getById(rs.getInt(4)));
			t.setId(rs.getInt(1));
			tables.add(t);
		}
		return tables;
	} 
}
