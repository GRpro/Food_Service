package com.dao.access;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
	
	private String config;
	private String driver;
	private Connection connection;
	
	public Connector(String path) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(path);){
			prop.load(fis);
			String url = prop.getProperty("url");
			String db = prop.getProperty("db");
			String login = prop.getProperty("login");
			String password = prop.getProperty("password");
			this.driver = prop.getProperty("driver");
			this.config = url + db + "?user=" + login + "&password=" + password;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        try {
//        	System.out.println(driver);
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(config);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
