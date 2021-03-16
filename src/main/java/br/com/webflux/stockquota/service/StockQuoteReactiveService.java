package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockQuoteReactiveService {
    Mono<Stock> getStockByTicketName(String ticket);
    Mono<Stock> save(Stock stockQuote);
    Mono<StockDTO> searchByElasticClient(String ticket);
    Flux<Stock> searchByTemplate(String ticket);
}
