package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockQuoteReactiveService {
    Mono<Stock> getStockByTicketName(String ticket);
    Mono<Stock> save(Stock stockQuote);
    Flux<Stock> searchByElasticClient(String ticket);
}
