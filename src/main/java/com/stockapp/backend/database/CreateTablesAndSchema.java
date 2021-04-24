package com.stockapp.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateTablesAndSchema {
	
	public static void main(String[] args) {
		try(Connection conn = createNewDBconnection()){
			createSchemaAndTables(conn);
			System.out.println("Tables and schema created");
		}catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	private static void createSchemaAndTables(Connection connection) {
		try {
			connection.prepareStatement("DROP SCHEMA if exists adb1").execute();
			PreparedStatement statement = connection.prepareStatement("CREATE DATABASE adb1"); 
			statement.execute();
			connection.setSchema("adb1");
			String createCompany = "CREATE TABLE adb1.company ("+ 
					"					symbol varchar(25),"+ 
					"					companyName varchar(150)," + 
					"					Constraint PK Primary Key(symbol)"+ 
					"					)";
			String createExchange = "CREATE TABLE adb1.exchange (" + 
					"    companyName varchar(150)," + 
					"    symbol varchar(25)," + 
					"exchange varchar(50)" + 
					")";
			String createPrice = "CREATE TABLE adb1.price (" + 
					"    symbol varchar(25)," + 
					"    volume int," + 
					"    price float," + 
					"    openPrice float," + 
					"    lowPrice float," + 
					"    highPrice float," + 
					"    tradeDate date" + 
					")";
			String createTimeframe = "CREATE TABLE adb1.timeframe (" + 
					"    symbol varchar(25)," + 
					"    startDate date," + 
					"    endDate date" + 
					")";
			connection.prepareStatement(createCompany).execute();
			connection.prepareStatement(createExchange).execute();
			connection.prepareStatement(createPrice).execute();
			connection.prepareStatement(createTimeframe).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
