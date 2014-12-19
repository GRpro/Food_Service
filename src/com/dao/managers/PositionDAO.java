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
import com.dao.entities.Position;
import com.dao.entities.Rating;

public class PositionDAO extends DAO<Position> {
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private DishDAO dishDao;
	private OrderingDAO orderingDao;
	private CashDAO<Integer, Position> cash;
	
	public PositionDAO(Connection connection, DishDAO dishDao, OrderingDAO orderingDao) {
		super();
		this.connection = connection;
		this.dishDao = dishDao;
		this.orderingDao = orderingDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO position"
					+ "(dish, number, ordering)"
					+ "VALUES(?,?,?)");
			this.update = connection.prepareStatement("UPDATE position SET "
				+ "dish = ?, number = ?, ordering = ? "
				+ "WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM position WHERE id = ?");
			//load into cash
			this.cash = new CashDAO<Integer, Position>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public HashSet<Position> get(Condition<Position> condition)
			throws SQLException {
		return cash.get(condition);
	}

	@Override
	public Position create(Position object) throws SQLException {
		create.setInt(1, object.getDish().getId());
		create.setInt(2, object.getNumber());
		create.setInt(3, object.getOrdering().getId());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM position");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}

	@Override
	public Position update(Position object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getDish().getId());
		update.setInt(2, object.getNumber());
		update.setInt(3, object.getOrdering().getId());
		update.setInt(4, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}

	@Override
	public boolean delete(Position object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}

	@Override
	public Position getById(Object id) throws SQLException {
		return cash.getById(id);
	}

	@Override
	protected HashSet<Position> getFromDatabase() throws SQLException {
		HashSet<Position> positions = new HashSet<Position>();
		ResultSet rs = statement.executeQuery("SELECT * FROM position");	
		while (rs.next()) {
			Position p = new Position(dishDao.getById(rs.getInt(2)), 
					rs.getInt(3), orderingDao.getById(rs.getInt(4)));
			p.setId(rs.getInt(1));
			positions.add(p);
		}
		return positions;
	} 
}
