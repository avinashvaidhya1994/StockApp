package com.stockapp.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateTablesAndSchema {
	
	public static void main(String[] args) {
		try(Connection conn = createNewDBconnection()){
			
			System.out.println(conn.getSchema());
		}catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	private static String dbhost = "jdbc:mysql://localhost:3306";
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
