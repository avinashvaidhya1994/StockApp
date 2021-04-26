package com.stockapp.backend.database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
public class InsertionStockPrice {
	public static void main(String[] args) throws IOException, ParseException {
		
//		 File file = new File("D:\\MS_UCM\\AdvancedDB\\ProjectAdb\\Data\\history");
		 File file = new File("G:\\Project 1\\Detail Spec\\Data_updated\\history");
	        File[] files = file.listFiles();
	        int fileCount = 0;
	        for(File f: files){
	        	if(fileCount > 11) {
	        		break;
	        	}
	            String fileNameWithOutExt = FilenameUtils.removeExtension(f.getName());
	            System.out.println(f);
	            InsertionToData(f.getPath(),fileNameWithOutExt);	            	
	            fileCount++;
	        }
		
		
	}

	private static void InsertionToData(String csvFilePath,String inputSymbol) throws FileNotFoundException, IOException, NumberFormatException, ParseException {
		String sql_select = "INSERT INTO price (symbol,volume,price,openPrice,lowPrice,highPrice,tradeDate) VALUES (?, ?,?,?, ?,?,?)";
		int batchSize = 2000;
		
		try(Connection conn = DBConnection.createNewDBconnection()){
			
			conn.createStatement();
			PreparedStatement statement = conn.prepareStatement(sql_select);
			 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); // skip header line
            lineReader.readLine(); // skip next line
            int iteration = 0;
            int batchNumber = 0;
            while ((lineText = lineReader.readLine()) != null) {
//            	if(iteration == 0) {
//                    iteration++;  
//                    continue;
//                }
                String[] data = lineText.split(",");
                Long volume = Long.parseLong(data[2]);
                Float price = Float.valueOf(data[1]);
                Float open = Float.valueOf(data[3]);
                Float low = Float.valueOf(data[4]);
                Float high = Float.valueOf(data[5]);
                String tradeDate = parseDate(data[0]);
                java.sql.Date sqlDate = java.sql.Date.valueOf(tradeDate);
                statement.setString(1, inputSymbol);
                statement.setLong(2, volume);
                statement.setFloat(3, price);
                statement.setFloat(4, open);
                statement.setFloat(5, low);
                statement.setFloat(6, high);
                statement.setDate(7,sqlDate );
//                statement.addBatch();
                statement.execute();
                count++;
                
//                if (count == batchSize) {
//                	count = 0;
//                	batchNumber++;
//                	System.out.println("Batch number : " +batchNumber);
//                    statement.executeBatch();
//                    statement.clearBatch();
//                }
            }
 
            lineReader.close();
//            statement.executeBatch();
//            statement.clearBatch();
            //conn.commit();
            conn.close();
		    
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private static String parseDate(String date) throws ParseException{
		String newDate = null;
		if(date.contains("/")) {
		Date dtDob = new Date(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		newDate = sdf.format(dtDob);
		}else {
			//.String start_dt = "2011-01-01";
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
//			Date dateOne = (Date)formatter.parse(date);
//			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
//			 newDate = newFormat.format(dateOne);
			newDate = date;
		}
		return newDate;
	}

}
