package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import reactor.core.publisher.Flux;

public interface StockQuoteElasticTemplateService {
    Flux<Stock> searchByTemplate(String ticket);
}
