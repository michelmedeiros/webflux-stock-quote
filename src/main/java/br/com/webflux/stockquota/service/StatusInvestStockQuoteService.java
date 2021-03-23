package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;

import java.util.List;

public interface StatusInvestStockQuoteService {
    List<Stock> getStatusInvestStockQuote(String ticket);
}
