package com.stockapp.backend.entity;

import java.sql.Date;

public class Summary {
	
	private String symbol;
	
	private String companyName;
	
	private String exchange;
	
	private Date startDate;
	
	private Date endDate;
	
	private String highestPriceAndDate;
	
	private String highestVolumeAndDate;

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

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getHighestPriceAndDate() {
		return highestPriceAndDate;
	}

	public void setHighestPriceAndDate(String highestPriceAndDate) {
		this.highestPriceAndDate = highestPriceAndDate;
	}

	public String getHighestVolumeAndDate() {
		return highestVolumeAndDate;
	}

	public void setHighestVolumeAndDate(String highestVolumeAndDate) {
		this.highestVolumeAndDate = highestVolumeAndDate;
	}
		
}
