package com.stockapp.backend.service;

import java.sql.Connection;

import org.springframework.stereotype.Component;

import com.stockapp.backend.database.DBConnection;

@Component
public class CoreComponent {

	private Connection connection;
		
	public CoreComponent() {
		this.connection = DBConnection.createNewDBconnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
}
