package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.repository.StockQuoteReactiveRepository;
import br.com.webflux.stockquota.service.StockQuoteReactiveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;


@Slf4j
@Service
@AllArgsConstructor
public class StockQuoteReactiveServiceImpl implements StockQuoteReactiveService {

    private final StockQuoteReactiveRepository stockQuoteRepository;
    private final ReactiveElasticsearchClient client;
    private final ObjectMapper objectMapper;
    private final ReactiveElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Flux<Stock> searchByTemplate(String ticket) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("ticket", ticket.toUpperCase()))
                .build();
        final Flux<SearchHit<Stock>> stockFlux = elasticsearchTemplate
                .search(searchQuery, Stock.class, IndexCoordinates.of("stock_idx"));
        return stockFlux.map(SearchHit::getContent);
    }

    @Override
    public Flux<Stock> searchAll() {
        return stockQuoteRepository.findAll();
    }

    @Override
    public Mono<Stock> getStockByTicketName(String ticket) {
        return stockQuoteRepository.findFirstByTicket(ticket);
    }

    @Override
    public Mono<Stock> save(StockDTO stockQuote) {
        return stockQuoteRepository.save(StockConverter.convertEntity(stockQuote));
    }

    @Override
    public Mono<StockDTO> searchByElasticClient(String ticket) {
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("ticket", ticket));
        final SearchRequest searchRequest = new SearchRequest().indices("stock_idx")
                .source(searchSourceBuilder);

        final Mono<SearchResponse> searchResponseMono = client.searchForResponse(searchRequest);

        try {
            return parseResponse(searchResponseMono);
        }catch (JsonProcessingException ex) {
            log.error("Error processing data", ex);
        }

        return null;
    }

    private Mono<StockDTO> parseResponse(Mono<SearchResponse> searchResponseMono) throws JsonProcessingException {
        final SearchResponse searchResponse = searchResponseMono.share().block();
        if(Objects.nonNull(searchResponse) && RestStatus.OK.equals(searchResponse.status())) {
            return parseHits(searchResponse.getHits());
        } else {
            log.error("Failt to return results");
        }
        return Mono.empty();
    }

    private Mono<StockDTO> parseHits(SearchHits hits) throws JsonProcessingException {
        if(hits.getTotalHits().value > 0) {
            String searchResult = hits.getAt(0).getSourceAsString();
            var stock = objectMapper.readValue(searchResult, StockDTO.class);
            return Mono.just(stock);
        }
        return Mono.empty();
    }

}
