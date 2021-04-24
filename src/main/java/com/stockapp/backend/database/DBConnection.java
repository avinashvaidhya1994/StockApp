package com.stockapp.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
	
	private static String dbhost = "jdbc:mysql://localhost:3306/adb1";
	private static String username = "root";
//	private static String password = "Shimsha06$";
	private static String password = "root";
	private static Connection conn;
	
	@SuppressWarnings("finally")
	public static Connection createNewDBconnection() {
		try  {	
			conn = DriverManager.getConnection(
					dbhost, username, password);	
		} catch (SQLException e) {
			System.out.println("Cannot create database connection");
			e.printStackTrace();
		} finally {
			return conn;	
		}		
	}

}
