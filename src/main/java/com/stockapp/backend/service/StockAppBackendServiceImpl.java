package com.stockapp.backend.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockapp.backend.entity.Price;
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
			while(rs2.next() && index < pastDaysCount) {
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
				priceList.add(price);
			}
			resultMap.put("statisticsDetail", priceList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultMap;
	}
		
}
