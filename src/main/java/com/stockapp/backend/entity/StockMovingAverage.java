package com.stockapp.backend.entity;

import java.sql.Date;

public class StockMovingAverage {
	
	private String symbol;
	
	private String companyName;
	
	private float price;
	
	private float fiftyDayAverage;
	
	private float twoHundredDayAverage;
	
	private Date tradeDate;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getFiftyDayAverage() {
		return fiftyDayAverage;
	}

	public void setFiftyDayAverage(float fiftyDayAverage) {
		this.fiftyDayAverage = fiftyDayAverage;
	}

	public float getTwoHundredDayAverage() {
		return twoHundredDayAverage;
	}

	public void setTwoHundredDayAverage(float twoHundredDayAverage) {
		this.twoHundredDayAverage = twoHundredDayAverage;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}	
	
}
