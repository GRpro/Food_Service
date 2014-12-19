package com.test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dao.abstractions.Condition;
import com.dao.access.*;
import com.dao.entities.*;
import com.dao.managers.*;
import com.main.TrueCondition;

public class TestFoodServiceDAO {
	
	public static UserDAO userDao;
	public static FriendsDAO friendsDao;
	public static TableDAO tableDao;
	public static RatingDAO ratingDao;
	public static MessageDAO messageDao;
	public static OrderingDAO orderingDao;
	public static PhotoDAO photoDao;
	public static ReceiverDAO receiverDao;
	public static BlockDAO blockDao;
	public static DiscountDAO discountDao;
	public static DishDAO dishDao;
	public static PositionDAO positionDao;
	public static EPDAO epDao;
	public static EPPhotoDAO epPhotoDao;
	public static TablePhotoDAO tablePhotoDao;
	public static DishPhotoDAO dishPhotoDao;
	
	public static TrueCondition tc;
	
	User u1 = new User("Grigoriy", "Roghkov", "login1", "password1", 1, "M", "personal data");
	User u2 = new User("Petro", "Shevchick", "login2", "password2", 1, "M", "personal data");
	User u3 = new User("Andrew", "Grigorenko", "login3", "password3", 1, "M", "personal data");
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		userDao = DAOFactory.getUserDAOInstance();
		friendsDao =DAOFactory.getFriendsDAOInstance();
		tableDao = DAOFactory.getTableDAOInstance();
		ratingDao = DAOFactory.getRatingDAOInstance();
		messageDao = DAOFactory.getMessageDAOInstance();
		orderingDao = DAOFactory.getOrderingDAOInstance();
		photoDao = DAOFactory.getPhotoDAOInstance();
		receiverDao = DAOFactory.getReceiverDAOInstance();
		blockDao = DAOFactory.getBlockDAOInstance();
		discountDao = DAOFactory.getDiscountDAOInstance();
		dishDao = DAOFactory.getDishDAOInstance();
		positionDao = DAOFactory.getPositionDAOInstance();
		epDao = DAOFactory.getEpDAOInstance();
		epPhotoDao = DAOFactory.getEpPhotoDAOInstance();
		tablePhotoDao = DAOFactory.getTablePhotoDAOInstance();
		dishPhotoDao = DAOFactory.getDishPhotoDAOInstance();
		
		tc = new TrueCondition<>();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		HashSet<User> u = userDao.get(tc);
		for (User user : u)
			userDao.delete(user);
		assertTrue(userDao.get(tc).size() == 0);
		
		HashSet<Message> m = messageDao.get(tc);
		for (Message message : m)
			messageDao.delete(message);
		assertTrue(messageDao.get(tc).size() == 0);
		
//		System.out.println(receiverDao.get(tc).size());
//		HashSet<Receiver> r = receiverDao.get(tc);
//		for (Receiver receiver : r)
//			receiverDao.delete(receiver);
//		assertTrue(receiverDao.get(tc).size() == 0);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUser() throws SQLException {
		userDao.create(u1);
		userDao.create(u2);
		userDao.create(u3);
		assertTrue(userDao.get(tc).size() == 3);
		String s1 = "password updated";
		u1.setPassword(s1);
		userDao.update(u1);
		assertEquals(u1.getId(), userDao.getById(u1.getId()).getId());
		userDao.delete(u1);
		assertTrue(userDao.get(tc).size() == 2);
		userDao.create(u1);
		HashSet<User> hs = userDao.get(new Condition<User>() {
			public boolean satisfyTo(User object) {
				return object.getLogin().equals("login2");
			}
		});
		assertEquals(1, hs.size());
		Object[] us = hs.toArray();
		assertEquals(u2.getFirstName(), ((User) us[0]).getFirstName());
		
		HashSet<User> u = userDao.get(tc);
		for (User user : u)
			userDao.delete(user);
		assertTrue(userDao.get(tc).size() == 0);
	}
	
	@Test
	public void testMessage() throws SQLException {
		userDao.create(u1);
		userDao.create(u2);
		userDao.create(u3);
		
		Message m1 = new Message("message1", u1, null, 1);
		messageDao.create(m1);
		Assert.assertTrue(messageDao.get(tc).size() == 1);
		Message m2 = new Message("message2", u2, null, 1);
		Message m3 = new Message("message3", u3, null, 1);
		messageDao.create(m2);
		messageDao.create(m3);
		Assert.assertTrue(messageDao.get(tc).size() == 3);
		m2.setMessage(m3);
		messageDao.update(m2);
		m1.setMessage(m2);
		messageDao.update(m1);
		Message ms = messageDao.getById(m1.getId());
		Assert.assertTrue(ms.getMessage().getId() == m2.getId());
		Assert.assertTrue(ms.getMessage().getMessage().getId() == m3.getId());	
		
		HashSet<Message> m = messageDao.get(tc);
		for (Message message : m)
			messageDao.delete(message);
		assertTrue(messageDao.get(tc).size() == 0);
		
		
		HashSet<User> u = userDao.get(tc);
		for (User user : u)
			userDao.delete(user);
		assertTrue(userDao.get(tc).size() == 0);
	}
	
	@Test
	public void testReceiver() throws SQLException {
		userDao.create(u1);
		userDao.create(u2);
		userDao.create(u3);
		Message m1 = new Message("message1", u1, null, 1);
		messageDao.create(m1);
		Receiver r1 = new Receiver(m1, u2);
		Receiver r2 = new Receiver(m1, u3);
		receiverDao.create(r1);
		receiverDao.create(r2);
		assertTrue(receiverDao.get(tc).size() == 2);
		assertTrue(receiverDao.getById(r1.getId()).getMessage().getId() ==
				receiverDao.getById(r2.getId()).getMessage().getId());
		receiverDao.delete(r1);
		assertTrue(receiverDao.get(tc).size() == 1);
		r2.setUser(u2);
		receiverDao.update(r2);
		assertTrue(receiverDao.getById(r2.getId()).getUser().getId() == u2.getId());
		
		HashSet<Message> m = messageDao.get(tc);
		for (Message message : m)
			messageDao.delete(message);
		assertTrue(messageDao.get(tc).size() == 0);
		
		HashSet<User> u = userDao.get(tc);
		for (User user : u)
			userDao.delete(user);
		assertTrue(userDao.get(tc).size() == 0);
	}

}
