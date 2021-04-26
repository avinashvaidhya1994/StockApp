package com.stockapp.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stockapp.backend.service.StockAppBackendService;

@CrossOrigin(origins = "http://localhost:3000")
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
	
	@GetMapping(value = "/getCurrentQuarterDetail/{symbol}")
	ResponseEntity<Map> getCurrentQuarterDetail(@PathVariable(name = "symbol",required = true) String symbol){
		Map<String,Object> resultMap = stockAppBackendService.getQuarterResultForSymbol(symbol);
		return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	}
	
	@GetMapping(value = "/getComparisonDetail/{symbol1}/{symbol2}")
	ResponseEntity<Map> getComparisonDetail(@PathVariable(name = "symbol1",required = true) String symbol1,
			@PathVariable(name = "symbol2",required = true) String symbol2){
		Map<String,Object> resultMap = stockAppBackendService.getComparisonForSymbols(symbol1,symbol2);
		return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	}
	
	@GetMapping(value = "/getMovingAverageDetail/{symbol}")
	ResponseEntity<Map> getMMovingAverageDetail(@PathVariable(name = "symbol",required = true) String symbol){
		Map<String,Object> resultMap = stockAppBackendService.getMovingAverageForSymbol(symbol);
		return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	}
	
}
