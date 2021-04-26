package com.stockapp.backend.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockapp.backend.entity.Price;
import com.stockapp.backend.entity.StockMovingAverage;
import com.stockapp.backend.entity.Summary;

@Service
public class StockAppBackendServiceImpl implements StockAppBackendService{

	private CoreComponent coreComponent;
	
	@Autowired
	public StockAppBackendServiceImpl(CoreComponent coreComponent) {
		this.coreComponent = coreComponent;
	}

	@Override
	public Map<String,Object> getSummaryForSymbol(String symbol) {
		Map<String, Object> resultMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		String highestPriceSelect = "Select * from price where symbol = ? and highPrice = (select max(highPrice) from price where symbol = ?)";
		String highestVolumeSelect = "Select * from price where symbol = ? and volume = (select max(volume) from price where symbol = ?)";
		String timeframeSelet = "Select * from timeframe where symbol = ?";
		Summary summary = new Summary();
		try {
			PreparedStatement exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				resultMap.put("error", "Symbol: " + symbol + " , not found");
				return resultMap;
			}
			summary.setSymbol(symbol);
			summary.setCompanyName(rs.getString("companyName"));
			summary.setExchange(rs.getString("exchange"));
			PreparedStatement highestPriceSelectStatement = coreComponent.getConnection().prepareStatement(highestPriceSelect);
			highestPriceSelectStatement.setString(1, symbol);
			highestPriceSelectStatement.setString(2, symbol);
			ResultSet rs1 = highestPriceSelectStatement.executeQuery();
			if(!rs1.next()) {
				resultMap.put("error","No Data available for symbol : " + symbol);
				return resultMap;
			}
			String highestPrice = String.valueOf(rs1.getFloat("highPrice")) + " ( " + String.valueOf(rs1.getDate("tradeDate"))  + " )";
			summary.setHighestPriceAndDate(highestPrice);
			PreparedStatement highestVolumeSelectStatement = coreComponent.getConnection().prepareStatement(highestVolumeSelect);
			highestVolumeSelectStatement.setString(1, symbol);
			highestVolumeSelectStatement.setString(2, symbol);
			ResultSet rs2 = highestVolumeSelectStatement.executeQuery();
			if(!rs2.next()) {
				resultMap.put("error","No Data available for symbol : " + symbol);
				return resultMap;
			}
			String highestVolume = String.valueOf(rs2.getInt("volume")) + " ( " + String.valueOf(rs2.getDate("tradeDate"))  + " )";
			summary.setHighestVolumeAndDate(highestVolume);
			PreparedStatement timeframeSelectStatement = coreComponent.getConnection().prepareStatement(timeframeSelet);
			timeframeSelectStatement.setString(1, symbol);
			ResultSet rs3 = timeframeSelectStatement.executeQuery();
			if(!rs3.next()) {
				resultMap.put("error","No Data available for symbol : " + symbol);
				return resultMap;
			}
			summary.setStartDate(rs3.getDate("startDate"));
			summary.setEndDate(rs3.getDate("endDate"));
			resultMap.put("summaryDetail", summary);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getStatisticsForSymbol(String symbol, int pastDaysCount, String exchange) {
		Map<String, Object> resultMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ? and exchange = ?";
		String priceCountSelect = "Select count(*) from price where symbol = ?";
		String priceSelect = "Select * from price where symbol = ? order by tradeDate desc";
		try {
			PreparedStatement exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			exchangeSelectStatement.setString(2, exchange);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				resultMap.put("error", "Symbol: " + symbol + " , not found in exchange : " + exchange);
				return resultMap;
			}
			String companyName = rs.getString("companyName");
			PreparedStatement priceCountSelectStatement = coreComponent.getConnection().prepareStatement(priceCountSelect);
			priceCountSelectStatement.setString(1, symbol);
			ResultSet rs1 = priceCountSelectStatement.executeQuery();
			if(!rs1.next()) {
				resultMap.put("error", "No Statistical data fount for symbol: " + symbol);
				return resultMap;
			}
			int count = rs1.getInt(1);
			if(count < pastDaysCount) {
				resultMap.put("error", "Input days cannot be greater than past available days");
				return resultMap;
			}
			PreparedStatement priceSelectStatement = coreComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int index = 0;
			List<Price> priceList = new ArrayList<>();
			Date prevDate = null;
			while(rs2.next() && index < pastDaysCount) {
				index++;
				Date tradeDate = rs2.getDate("tradeDate");
				if(prevDate != null && tradeDate.equals(prevDate)) {
					resultMap.put("error", "There are multiple price records for the date: " + String.valueOf(prevDate) + ", please check the data");
					return resultMap;
				}
				prevDate = tradeDate;
				Price price = new Price();
				price.setCompanyName(companyName);
				price.setSymbol(symbol);
				price.setOpenPrice(rs2.getFloat("openPrice"));
				price.setClosePrice(rs2.getFloat("price"));
				price.setHighPrice(rs2.getFloat("highPrice"));
				price.setLowPrice(rs2.getFloat("lowPrice"));
				price.setVolume(rs2.getInt("volume"));
				price.setTradeDate(rs2.getDate("tradeDate"));
				priceList.add(price);
			}
			resultMap.put("statisticsDetail", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getQuarterResultForSymbol(String symbol) {
		Map<String, Object> resultMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		String priceSelect = "Select * from price where symbol = ? order by tradeDate desc";
		try {
			PreparedStatement exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				resultMap.put("error", "Symbol: " + symbol + " , not found ");
				return resultMap;
			}
			String companyName = rs.getString("companyName");
			PreparedStatement priceSelectStatement = coreComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int index = 0;
			List<Price> priceList = new ArrayList<>();
			Date prevDate = null;
			int prevYear = 0;
			while(rs2.next()) {
				index++;
				Date tradeDate = rs2.getDate("tradeDate");
				int month = tradeDate.toLocalDate().getMonthValue();
				int year = tradeDate.toLocalDate().getYear();
				if(prevDate != null && tradeDate.equals(prevDate)) {
					resultMap.put("error", "There are multiple price records for the date: " + String.valueOf(prevDate) + ", please check the data");
					return resultMap;
				}					
				prevDate = tradeDate;
				if((index > 31 && month % 3 == 0 ) || (prevYear!= 0 && year != prevYear))
					break;
				prevYear = year;
				Price price = new Price();
				price.setCompanyName(companyName);
				price.setSymbol(symbol);
				price.setOpenPrice(rs2.getFloat("openPrice"));
				price.setClosePrice(rs2.getFloat("price"));
				price.setHighPrice(rs2.getFloat("highPrice"));
				price.setLowPrice(rs2.getFloat("lowPrice"));
				price.setVolume(rs2.getInt("volume"));
				price.setTradeDate(tradeDate);
				priceList.add(price);
			}
			if(priceList.isEmpty()) {
				resultMap.put("error", "No data found for Symbol : " + symbol);
				return resultMap;
			}
			priceList.sort(Comparator.comparing(Price::getTradeDate));
			resultMap.put("currentQuarterDetail", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getComparisonForSymbols(String symbol1, String symbol2) {
		Map<String, Object> resultMap = new HashMap<>();
		String company1Name = getCompanyName(symbol1);
		if(company1Name.equals("error")) {
			resultMap.put(company1Name, "Symbol : " + symbol1 + " Not found");
			return resultMap;
		}
		String company2Name = getCompanyName(symbol2);
		if(company2Name.equals("error")) {
			resultMap.put(company2Name, "Symbol : " + symbol2 + " Not found");
			return resultMap;
		}
		List<Price> symbol1PriceList = getPriceDetails(resultMap,symbol1,company1Name);
		if(resultMap.get("error") != null)
			return resultMap;
		if(symbol1PriceList.isEmpty()) {
			resultMap.put("error", "No data found for Symbol : " + symbol1);
			return resultMap;
		}
		List<Price> symbol2PriceList = getPriceDetails(resultMap, symbol2,company2Name);
		if(symbol2PriceList.isEmpty()) {
			resultMap.put("error", "No data found for Symbol : " + symbol2);
			return resultMap;
		}
		resultMap.put("symbol1", symbol1PriceList);
		resultMap.put("symbol2", symbol2PriceList);
		return resultMap;
	}
	
	private String getCompanyName(String symbol) {
		String exchangeSelect = "Select * from exchange where symbol = ?";
		PreparedStatement exchangeSelectStatement;
		String companyName = "";
		try {
			exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				return "error";
			}
			 companyName = rs.getString("companyName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyName;
	}
	
	private List<Price> getPriceDetails(Map<String, Object> resultMap, String symbol,String companyName){
		String priceSelect = "Select * from price where symbol = ? order by tradeDate asc";
		List<Price> priceList = new ArrayList<>();
		try {
			PreparedStatement priceSelectStatement = coreComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			int index = 0;
			float firstPrice = 0f;
			float percentage = 0f;
			Date prevDate = null;
			while(rs2.next()) {
				Date tradeDate = rs2.getDate("tradeDate");
				if(prevDate != null && tradeDate.equals(prevDate)) {
					String message = "For symbol : " + symbol +"there are multiple price records for the date: " + String.valueOf(prevDate) 
					+ ", please check the data";
					resultMap.put("error",message );
					return priceList;
				}
				prevDate = tradeDate;
				if(index < 1) {
					firstPrice = rs2.getFloat("price");
					percentage = 0f;
				}else {
					percentage = (( rs2.getFloat("price") - firstPrice)/firstPrice) * 100;
				}
				index++;
				Price price = new Price();
				price.setCompanyName(companyName);
				price.setSymbol(symbol);
				price.setOpenPrice(rs2.getFloat("openPrice"));
				price.setClosePrice(rs2.getFloat("price"));
				price.setHighPrice(rs2.getFloat("highPrice"));
				price.setLowPrice(rs2.getFloat("lowPrice"));
				price.setVolume(rs2.getInt("volume"));
				price.setTradeDate(rs2.getDate("tradeDate"));
				price.setPercentageChangeFromFirst(percentage);
				priceList.add(price);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return priceList;
		
	}

	@Override
	public Map<String, Object> getMovingAverageForSymbol(String symbol,int years) {
		Map<String, Object> resultMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		String priceSelect = "Select * from price where symbol = ? order by tradeDate desc";
		try {
			PreparedStatement exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				resultMap.put("error", "Symbol: " + symbol + " , not found ");
				return resultMap;
			}
			String companyName = rs.getString("companyName");
			PreparedStatement priceSelectStatement = coreComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			ResultSet rs2 = priceSelectStatement.executeQuery();
			List<StockMovingAverage> movingAverageList = new ArrayList<>();
			Date prevDate = null;		
			int count = 0;			
			while(rs2.next() && count < years*365) {
				count++;
				Date tradeDate = rs2.getDate("tradeDate");
				if(prevDate != null && tradeDate.equals(prevDate)) {
					resultMap.put("error", "There are multiple price records for the date: " + String.valueOf(prevDate) + ", please check the data");
					return resultMap;
				}
				prevDate = tradeDate;
				StockMovingAverage stockMovingAverage = new StockMovingAverage();
				stockMovingAverage.setCompanyName(companyName);
				stockMovingAverage.setSymbol(symbol);
				stockMovingAverage.setPrice(rs2.getFloat("price"));
				stockMovingAverage.setTradeDate(tradeDate);
				movingAverageList.add(stockMovingAverage);
			}
			if(movingAverageList.isEmpty()) {
				resultMap.put("error", "No data found for Symbol : " + symbol);
				return resultMap;
			}
//			if(movingAverageList.size() < years*365) {
//				resultMap.put("error", "No data found for Symbol : " + symbol + " , for the last " + years + " years. Please select valid range");
//				return resultMap;
//			}
			
			movingAverageList.sort(Comparator.comparing(StockMovingAverage::getTradeDate));
			System.out.println(movingAverageList.size());
			float fiftyDaySum = 0;
			float twoHundredDaySum = 0;
			for (int i = 0; i < movingAverageList.size(); i++) {
				fiftyDaySum += movingAverageList.get(i).getPrice();
				if(i >= 50) 
					fiftyDaySum -= movingAverageList.get(i-50).getPrice();
				float fiftyDayAverage = i >= 50 ? fiftyDaySum / 50 : i==0 ? fiftyDaySum : fiftyDaySum / i;
				twoHundredDaySum += movingAverageList.get(i).getPrice();
				if(i >= 200) 
					twoHundredDaySum -= movingAverageList.get(i-200).getPrice();
				float twoHundredDayAverage = i >= 200 ? twoHundredDaySum / 200 : i==0 ? twoHundredDaySum : twoHundredDaySum / i;
				movingAverageList.get(i).setFiftyDayAverage(fiftyDayAverage);
				movingAverageList.get(i).setTwoHundredDayAverage(twoHundredDayAverage);
			}
			resultMap.put("movingAverageDetail", movingAverageList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getFinancialDetailForSymbol(String symbol, String fromDate, String toDate) {
		Map<String, Object> resultMap = new HashMap<>();
		String exchangeSelect = "Select * from exchange where symbol = ?";
		String priceSelect = "Select * from price where symbol = ? and tradeDate between ? and ? order by tradeDate asc";
		try {
			PreparedStatement exchangeSelectStatement = coreComponent.getConnection().prepareStatement(exchangeSelect);
			exchangeSelectStatement.setString(1, symbol);
			ResultSet rs = exchangeSelectStatement.executeQuery();
			if(!rs.next()) {
				resultMap.put("error", "Symbol: " + symbol + " , not found ");
				return resultMap;
			}
			String companyName = rs.getString("companyName");
			PreparedStatement priceSelectStatement = coreComponent.getConnection().prepareStatement(priceSelect);
			priceSelectStatement.setString(1, symbol);
			priceSelectStatement.setDate(2, Date.valueOf(fromDate));
			priceSelectStatement.setDate(3, Date.valueOf(toDate));
			ResultSet rs2 = priceSelectStatement.executeQuery();
			List<Price> priceList = new ArrayList<>();
			Date prevDate = null;		
			while(rs2.next()) {
				Date tradeDate = rs2.getDate("tradeDate");
				if(prevDate != null && tradeDate.equals(prevDate)) {
					resultMap.put("error", "There are multiple price records for the date: " + String.valueOf(prevDate) + ", please check the data");
					return resultMap;
				}					
				prevDate = tradeDate;
				Price price = new Price();
				price.setCompanyName(companyName);
				price.setSymbol(symbol);
				price.setOpenPrice(rs2.getFloat("openPrice"));
				price.setClosePrice(rs2.getFloat("price"));
				price.setHighPrice(rs2.getFloat("highPrice"));
				price.setLowPrice(rs2.getFloat("lowPrice"));
				price.setVolume(rs2.getInt("volume"));
				price.setTradeDate(tradeDate);
				priceList.add(price);
			}
			if(priceList.isEmpty()) {
				resultMap.put("error", "No data found for Symbol : " + symbol);
				return resultMap;
			}		
			
			resultMap.put("financialDetail", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultMap;
	}
		
}
