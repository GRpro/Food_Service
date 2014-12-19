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
import com.dao.entities.Dish;
import com.dao.entities.Rating;
import com.dao.entities.Table;

public class DishDAO extends DAO<Dish> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;

	private EPDAO epDao;
	private CashDAO<Integer, Dish> cash;
	
	public DishDAO(Connection connection, EPDAO epDao) {
		super();
		this.connection = connection;
		this.epDao = epDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO dish"
					+ "(name, cuisine, price, ep)"
					+ "VALUES(?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE dish SET "
				+ "name = ?, cuisine = ?, price = ?, ep = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM dish WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer, Dish>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public HashSet<Dish> get(Condition<Dish> condition) throws SQLException {
		return cash.get(condition);
	}
	
	@Override
	public Dish create(Dish object) throws SQLException {
		create.setString(1, object.getName());
		create.setString(2, object.getCuisine());
		create.setFloat(3, object.getPrice());;
		create.setInt(4, object.getEp().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM dish");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	@Override
	public Dish update(Dish object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getName());
		update.setString(2, object.getCuisine());
		update.setFloat(3, object.getPrice());;
		update.setInt(4, object.getEp().getId());
		update.setInt(5, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Dish object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public Dish getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Dish> getFromDatabase() throws SQLException {
		HashSet<Dish> dishes = new HashSet<Dish>();
		ResultSet rs = statement.executeQuery("SELECT * FROM dish");	
		while (rs.next()) {
			Dish d =  new Dish(rs.getString(2), rs.getString(3), 
					rs.getFloat(4), epDao.getById(rs.getInt(5)));
			d.setId(rs.getInt(1));
			dishes.add(d);
		}
		return dishes;
	} 
	
	
}
