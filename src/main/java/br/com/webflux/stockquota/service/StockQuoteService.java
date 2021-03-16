package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;

import java.util.List;

public interface StockQuoteService {
    List<Stock> getStockQuote(String ticket);
}
