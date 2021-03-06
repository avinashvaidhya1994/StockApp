package com.stockapp.backend.service;

import java.util.Map;

import com.stockapp.backend.entity.Summary;

public interface StockAppBackendService {

	Map<String,Object> getSummaryForSymbol(String symbol);

	Map<String, Object> getStatisticsForSymbol(String symbol, int pastDaysCount, String exchange);

	Map<String, Object> getQuarterResultForSymbol(String symbol);

	Map<String, Object> getComparisonForSymbols(String symbol1, String symbol2);

	Map<String, Object> getMovingAverageForSymbol(String symbol,int years);

	Map<String, Object> getFinancialDetailForSymbol(String symbol, String fromDate, String toDate);

	Map<String, Object> getMonthlyDetailForSymbol(String symbol, String fromDate, String toDate, int lotSize);
	
}
