package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface YahooFinancialQuoteService {
    Mono<Stock> getYahooFinanceStockQuote(String ticket);
    Mono<Stock> generateYahooFinanceStockQuote(String ticket);
}
