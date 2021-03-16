package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.StockQuoteElasticTemplateService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockQuoteElasticTemplateServiceImpl implements StockQuoteElasticTemplateService {

    private final ReactiveElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Flux<Stock> searchByTemplate(String ticket) {
        return null;
    }
//    @Autowired
//    private final ReactiveElasticsearchTemplate elasticsearchTemplate;
//
//    @Override
//    public Flux<Stock> searchByTemplate(String ticket) {
//        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchQuery("ticket", ticket.toUpperCase()))
//                .build();
//        final Flux<SearchHit<Stock>> stockFlux = elasticsearchTemplate
//                .search(searchQuery, Stock.class, IndexCoordinates.of("stock_idx"));
//        return stockFlux.map(SearchHit::getContent);
//    }
}
