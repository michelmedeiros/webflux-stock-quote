package br.com.webflux.stockquota.repository;

import br.com.webflux.stockquota.domain.Stock;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockQuoteRepository extends ReactiveElasticsearchRepository<Stock, String> {
}
