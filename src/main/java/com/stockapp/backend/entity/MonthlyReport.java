package com.stockapp.backend.entity;

public class MonthlyReport {

	private String symbol;
	
	private String companyName;
	
	private int year;
	
	private int month;
	
	private float glPerShare;
	
	private int lotSize;
	
	private float glPercentage;
	
	private float glPerLot;

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public float getGlPerShare() {
		return glPerShare;
	}

	public void setGlPerShare(float glPerShare) {
		this.glPerShare = glPerShare;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public float getGlPercentage() {
		return glPercentage;
	}

	public void setGlPercentage(float glPercentage) {
		this.glPercentage = glPercentage;
	}

	public float getGlPerLot() {
		return glPerLot;
	}

	public void setGlPerLot(float glPerLot) {
		this.glPerLot = glPerLot;
	}
		
}
