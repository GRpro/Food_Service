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
import com.dao.entities.Discount;
import com.dao.entities.Rating;

public class DiscountDAO extends DAO<Discount> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private DishDAO dishDao;
	private CashDAO<Integer, Discount> cash;
	
	public DiscountDAO(Connection connection, DishDAO dishDao) {
		super();
		this.connection = connection;
		this.dishDao = dishDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO discount"
					+ "(startTime, endTime, persentage, dish)"
					+ "VALUES(?,?,?,?)");
			this.update = connection.prepareStatement("UPDATE discount SET "
				+ "startTime = ?, endTime = ?, persentage = ?, dish = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM discount WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer, Discount>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashSet<Discount> get(Condition<Discount> condition)
			throws SQLException {
		return cash.get(condition);
	}
	
	@Override
	public Discount create(Discount object) throws SQLException {
		create.setString(1, object.getStartTime());
		create.setString(2, object.getEndTime());
		create.setFloat(3, object.getPersentage());;
		create.setInt(4, object.getDish().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM discount");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	@Override
	public Discount update(Discount object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setString(1, object.getStartTime());
		update.setString(2, object.getEndTime());
		update.setFloat(3, object.getPersentage());;
		update.setInt(4, object.getDish().getId());
		update.setInt(5, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Discount object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public Discount getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Discount> getFromDatabase() throws SQLException {
		HashSet<Discount> discounts = new HashSet<Discount>();
		ResultSet rs = statement.executeQuery("SELECT * FROM discount");	
		while (rs.next()) {
			Discount d = new Discount(rs.getString(2), rs.getString(3), rs.getFloat(4), dishDao.getById(rs.getInt(5)));
			d.setId(rs.getInt(1));
			discounts.add(d);
		}
		return discounts;
	} 
}
