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
import com.dao.entities.Block;
import com.dao.entities.Friends;
import com.dao.entities.Table;

public class BlockDAO extends DAO<Block>{
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement update;
	private PreparedStatement create;
	private PreparedStatement delete;
	
	private TableDAO tableDao;
	private CashDAO<Integer, Block> cash;
	
	public BlockDAO(Connection connection, TableDAO tableDao) {
		super();
		this.connection = connection;
		this.tableDao = tableDao;
		try {
			this.statement = connection.createStatement();
			this.create = connection.prepareStatement("INSERT INTO block"
				+ "(table, startTime, endTime) VALUES(?,?,?)");
			this.update = connection.prepareStatement("UPDATE block SET "
					+ "table = ?, startTime = ?, endTime = ? WHERE id = ?");
			this.delete = connection.prepareStatement("DELETE FROM block WHERE id = ?");
			//load into cash
			cash = new CashDAO<>(getFromDatabase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public HashSet<Block> get(Condition<Block> condition) throws SQLException {
		return cash.get(condition);
	}
	
	@Override
	public Block create(Block object) throws SQLException {
		create.setInt(1, object.getTable().getId());
		create.setString(2, object.getStartTime());
		create.setString(3, object.getEndTime());
		if (create.executeUpdate() != 1) {
			throw new IllegalStateException();
		}
		ResultSet rs = statement.executeQuery("SELECT Max(id) FROM block");
		rs.next();
		object.setId(rs.getInt(1));
		//create in cash
		cash.create(object);
		return object;
	}
	
	@Override
	public Block update(Block object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		update.setInt(1, object.getTable().getId());
		update.setString(2, object.getStartTime());
		update.setString(3, object.getEndTime());
		update.setInt(4, object.getId());	
		if (update.executeUpdate() != 1)
			throw new IllegalStateException();
		//update in cash
		cash.update(object);
		return object;
	}
	
	@Override
	public boolean delete(Block object) throws SQLException {
		if (object.getId() == -1)
			throw new IllegalStateException();
		delete.setInt(1, object.getId());
		if (delete.executeUpdate() != 1)
			throw new IllegalStateException();
		//delete from cash
		return cash.delete(object);
	}
	
	@Override
	public Block getById(Object id) throws SQLException {
		return cash.getById(id);
	}
	
	@Override
	protected HashSet<Block> getFromDatabase() throws SQLException {
		HashSet<Block> blocks = new HashSet<Block>();
		ResultSet rs = statement.executeQuery("SELECT * FROM block");	
		while (rs.next()) {
			Block b = new Block(tableDao.getById(rs.getInt(2)), 
					rs.getString(3), rs.getString(4));
			b.setId(rs.getInt(1));
			blocks.add(b);
		}
		return blocks;
	} 
	
}
