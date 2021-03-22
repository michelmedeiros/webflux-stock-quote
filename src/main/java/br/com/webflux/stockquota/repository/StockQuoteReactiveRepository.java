package br.com.webflux.stockquota.repository;

import br.com.webflux.stockquota.domain.Stock;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockQuoteReactiveRepository extends ReactiveElasticsearchRepository<Stock, String> {
    Mono<Stock> findFirstByTicket(String ticket);
}
