package com.main;

import java.sql.SQLException;

import com.dao.abstractions.Condition;
import com.dao.access.DAOFactory;
import com.dao.entities.Friends;
import com.dao.entities.User;
import com.dao.managers.FriendsDAO;
import com.dao.managers.UserDAO;

public class Main {

	public static void main(String[] args) throws SQLException {
		

//		u1.setPassword("new password1");
//		u2.setLogin("login2 updated");
//		userDao.update(u1);
//		userDao.update(u2);
//		
//		Condition allUsers = new Condition<User>() {
//			@Override
//			public boolean satisfyTo(User object) {
//				return true;
//			}
//		};
//		
//		HashSet<User> ent = userDao.get(allUsers);
//		
//		userDao.get(all).forEach(v -> System.out.println(v.getId() +" " + v));
//		userDao.delete(u1);
//		userDao.get(all).forEach(v -> System.out.println(v.getId() +" " + v));
//		System.out.println("by id " + userDao.getById(u2.getId()));
//		userDao.delete(u2);
		
//		User u1 = userDao.getById(5);
////		User u2 = userDao.getById(6);
//		FriendsDAO friendsDao = f.getFriendsDAOInstance();
//		
//		Friends f1 = friendsDao.getById(3);
//		f1.setUser1(u1);
//		friendsDao.update(f1);
//		
////		System.out.println(f1);
////		friendsDao.create(f1);
//		
//		Condition<Friends> allFriends = new Condition<Friends>() {
//			@Override
//			public boolean satisfyTo(Friends object) {
//				return true;
//			}	
//		};
//		friendsDao.get(allFriends).forEach(v -> System.out.println(v));
//		
//		
	}

}
