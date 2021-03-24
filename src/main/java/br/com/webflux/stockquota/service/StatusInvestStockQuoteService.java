package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.integration.dto.StockStatisticsDTO;
import reactor.core.publisher.Flux;

import java.util.List;

public interface StatusInvestStockQuoteService {
    Flux<Stock> getStatusInvestStockQuote(String ticket);
    Flux<StockStatistics> generateStockStatistics();
}
