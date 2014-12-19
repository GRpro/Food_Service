package com.dao.access;
import com.dao.managers.BlockDAO;
import com.dao.managers.DiscountDAO;
import com.dao.managers.DishDAO;
import com.dao.managers.DishPhotoDAO;
import com.dao.managers.EPDAO;
import com.dao.managers.EPPhotoDAO;
import com.dao.managers.FriendsDAO;
import com.dao.managers.MessageDAO;
import com.dao.managers.OrderingDAO;
import com.dao.managers.PhotoDAO;
import com.dao.managers.PositionDAO;
import com.dao.managers.RatingDAO;
import com.dao.managers.ReceiverDAO;
import com.dao.managers.TableDAO;
import com.dao.managers.TablePhotoDAO;
import com.dao.managers.UserDAO;


public class DAOFactory {
	
	private static Connector connection = new Connector("config/config.properties");  
	
	private static BlockDAO blockDAOInstance;
	private static DiscountDAO discountDAOInstance;
	private static DishDAO dishDAOInstance;
	private static EPDAO epDAOInstance;
	private static FriendsDAO friendsDAOInstance;
	private static MessageDAO messageDAOInstance;
	private static OrderingDAO orderingDAOInstance;
	private static PhotoDAO photoDAOInstance;
	private static PositionDAO positionDAOInstance;
	private static RatingDAO ratingDAOInstance;
	private static ReceiverDAO receiverDAOInstance;
	private static TableDAO tableDAOInstance;
	private static UserDAO userDAOInstance;
	private static DishPhotoDAO dishPhotoDAOInstance;
	private static TablePhotoDAO tablePhotoDAOInstance;
	private static EPPhotoDAO epPhotoDAOInstance;
	
	public static DishPhotoDAO getDishPhotoDAOInstance() {
		if (dishPhotoDAOInstance == null) {
			dishPhotoDAOInstance = new DishPhotoDAO(connection.getConnection(), getDishDAOInstance(), getPhotoDAOInstance());
		}
		return dishPhotoDAOInstance;
	}
	public static TablePhotoDAO getTablePhotoDAOInstance() {
		if (tablePhotoDAOInstance == null) {
			tablePhotoDAOInstance = new TablePhotoDAO(connection.getConnection(), getTableDAOInstance(), getPhotoDAOInstance());
		}
		return tablePhotoDAOInstance;
	}
	public static EPPhotoDAO getEpPhotoDAOInstance() {
		if (epPhotoDAOInstance == null) {
			epPhotoDAOInstance = new EPPhotoDAO(connection.getConnection(), getEpDAOInstance(), getPhotoDAOInstance());
		}
		return epPhotoDAOInstance;
	}
	public static BlockDAO getBlockDAOInstance() {
		if (blockDAOInstance == null) {
			blockDAOInstance = new BlockDAO(connection.getConnection(), getTableDAOInstance());
		}
		return blockDAOInstance;
	}
	public static DiscountDAO getDiscountDAOInstance() {
		if (discountDAOInstance == null) {
			discountDAOInstance = new DiscountDAO(connection.getConnection(), getDishDAOInstance());
		}
		return discountDAOInstance;
	}
	public static DishDAO getDishDAOInstance() {
		if (dishDAOInstance == null) {
			dishDAOInstance = new DishDAO(connection.getConnection(), getEpDAOInstance());
		}
		return dishDAOInstance;
	}
	public static EPDAO getEpDAOInstance() {
		if (epDAOInstance == null) {
			epDAOInstance = new EPDAO(connection.getConnection());
		}
		return epDAOInstance;
	}
	public static FriendsDAO getFriendsDAOInstance() {
		if (friendsDAOInstance == null) {
			friendsDAOInstance = new FriendsDAO(connection.getConnection(), getUserDAOInstance());
		}
		return friendsDAOInstance;
	}
	public static MessageDAO getMessageDAOInstance() {
		if (messageDAOInstance == null) {
			messageDAOInstance = new MessageDAO(connection.getConnection(), getUserDAOInstance());
		}
		return messageDAOInstance;
	}
	public static OrderingDAO getOrderingDAOInstance() {
		if (orderingDAOInstance == null) {
			orderingDAOInstance = new OrderingDAO(connection.getConnection(), getUserDAOInstance());
		}
		return orderingDAOInstance;
	}
	public static PhotoDAO getPhotoDAOInstance() {
		if (photoDAOInstance == null) {
			photoDAOInstance = new PhotoDAO(connection.getConnection());
		}
		return photoDAOInstance;
	}
	public static PositionDAO getPositionDAOInstance() {
		if (positionDAOInstance == null) {
			positionDAOInstance = new PositionDAO(connection.getConnection(), getDishDAOInstance(), getOrderingDAOInstance());
		}
		return positionDAOInstance;
	}
	public static RatingDAO getRatingDAOInstance() {
		if (ratingDAOInstance == null) {
			ratingDAOInstance = new RatingDAO(connection.getConnection(), getUserDAOInstance(), getEpDAOInstance());
		}
		return ratingDAOInstance;
	}
	public static ReceiverDAO getReceiverDAOInstance() {
		if (receiverDAOInstance == null) {
			receiverDAOInstance = new ReceiverDAO(connection.getConnection(), getUserDAOInstance(), getMessageDAOInstance());
		}
		return receiverDAOInstance;
	}
	public static TableDAO getTableDAOInstance() {
		if (tableDAOInstance == null) {
			tableDAOInstance = new TableDAO(connection.getConnection(), getEpDAOInstance());
		}
		return tableDAOInstance;
	}
	public static UserDAO getUserDAOInstance() {
		if (userDAOInstance == null) {
			userDAOInstance = new UserDAO(connection.getConnection());
		}
		return userDAOInstance;
	}
}
