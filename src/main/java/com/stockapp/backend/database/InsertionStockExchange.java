package com.stockapp.backend.database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
public class InsertionStockExchange {
	public static void main(String[] args) throws IOException {
		
		String sql_select = "INSERT INTO exchange (symbol,exchange,companyName) VALUES (?, ?,?)";
		String csvFilePath = "D:\\MS_UCM\\AdvancedDB\\ProjectAdb\\Data\\exchange.csv";
		int batchSize = 20;
		
		try(Connection conn = DBConnection.createNewDBconnection()){
			
			conn.createStatement();
			PreparedStatement statement = conn.prepareStatement(sql_select);
			 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); // skip header line
            
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String symbol = data[0];
                String exchange = data[1];
                String companyName = data[2];
                statement.setString(1, symbol);
                statement.setString(2, exchange);
                statement.setString(3, companyName);
                statement.addBatch();
                
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
 
            lineReader.close();
            statement.executeBatch();
            //conn.commit();
            conn.close();
		    
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
