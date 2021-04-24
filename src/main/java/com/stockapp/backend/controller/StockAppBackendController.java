package com.stockapp.backend.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stockapp.backend.service.CoreComponent;
import com.stockapp.backend.service.StockAppBackendService;

@Controller
@RequestMapping("/stockapp")
public class StockAppBackendController {
	
	@Autowired
	StockAppBackendService stockAppBackendService;
	
	public StockAppBackendController(StockAppBackendService stockAppBackendService) {
		this.stockAppBackendService = stockAppBackendService;
	}
	
	@GetMapping(value = "/getSummaryDetail/{symbol}")
	ResponseEntity<Map> getSummaryDetails(@PathVariable(name = "symbol",required = true) String symbol){
		Map<String,Object> resultMap = stockAppBackendService.getSummaryForSymbol(symbol);
		return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	}
	
	@GetMapping(value = "/getStatisticsDetail/{symbol}")
	ResponseEntity<Map> getStatisticsDetail(@PathVariable(name = "symbol",required = true) String symbol,
			@RequestParam(name = "pastDaysCount",required = true) int pastDaysCount,
			@RequestParam(name= "exchange",required = true) String exchange){
		Map<String,Object> resultMap = stockAppBackendService.getStatisticsForSymbol(symbol,pastDaysCount,exchange);
		return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	}
	
}
