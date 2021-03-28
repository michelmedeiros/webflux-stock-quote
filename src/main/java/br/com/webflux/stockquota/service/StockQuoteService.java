package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StockQuoteService {
    Mono<Stock> getStockByTicketName(String ticket);
    Mono<Stock> save(StockDTO stockQuote);
    Mono<Stock> save(Stock stockQuote);
    Mono<StockDTO> searchByElasticClient(String ticket);
    Flux<Stock> searchByTemplate(String ticket);
    Flux<Stock> searchAll();
    Flux<Stock> saveAll(List<StockDTO> stocks);
}
