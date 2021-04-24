package com.stockapp.backend.service;

import java.util.Map;

import com.stockapp.backend.entity.Summary;

public interface StockAppBackendService {

	Map<String,Object> getSummaryForSymbol(String symbol);

	Map<String, Object> getStatisticsForSymbol(String symbol, int pastDaysCount, String exchange);
	
}
