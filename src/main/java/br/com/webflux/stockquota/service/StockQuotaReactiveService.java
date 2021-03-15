package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockQuotaDTO;
import br.com.webflux.stockquota.repository.StockQuoteReactiveRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;


@Slf4j
@Service
@AllArgsConstructor
public class StockQuotaReactiveService {

    @Autowired
    private final StatusInvestClient statusInvestClient;
    @Autowired
    private final StockQuoteReactiveRepository stockQuoteRepository;
    @Autowired
    private final ReactiveElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private final ReactiveElasticsearchClient client;
    @Autowired
    private final ObjectMapper objectMapper;

    public List<Stock> getStockQuote(String ticket) {
        try {
            final List<StockQuotaDTO> stocks = statusInvestClient.getStock(ticket);
            return stocks.stream().map(this::convertEntity)
                            .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        }
        return null;
    }

    private Stock convertEntity(StockQuotaDTO stock) {
        return Stock.builder()
                .quote(StockQuote.builder()
                        .price(new BigDecimal(stock.getPrice().replaceAll(",", ".")))
                        .build())
                .ticket(stock.getCode())
                .build();
    }

    public Mono<Stock> save(Stock stockQuote) {
        return stockQuoteRepository.save(stockQuote);
    }

    public Flux<Stock> searchByTemplate(String ticket) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("ticket", ticket.toUpperCase()))
                .build();
        final Flux<SearchHit<Stock>> stockFlux = elasticsearchTemplate
                .search(searchQuery, Stock.class, IndexCoordinates.of("stock_idx"));
        return stockFlux.map(SearchHit::getContent);
    }

    public Flux<Stock> searchBySpringData(String ticket) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("ticket", ticket.toUpperCase()))
                .build();

        final Flux<SearchHit<Stock>> stockHits = elasticsearchTemplate
                .search(searchQuery, Stock.class, IndexCoordinates.of("stock_idx"));

        return stockHits.map(SearchHit::getContent);
    }

    public Flux<Stock> searchByElaticClient(String ticket) throws JsonProcessingException {
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("ticket", ticket.toUpperCase()));
        final SearchRequest searchRequest = new SearchRequest().indices("stock_idx")
                .source(searchSourceBuilder);

        final Flux<org.elasticsearch.search.SearchHit> searchHits = client.search(searchRequest);

        final Map<String, Stock> stringStockMap = parseHits(searchHits);

        return Flux.empty();
    }

    private Map<String, Stock> parseHits(Flux<org.elasticsearch.search.SearchHit> searchHits) throws JsonProcessingException {
        final List<Map<String, SearchHits>> mapList = searchHits.toStream().map(org.elasticsearch.search.SearchHit::getInnerHits).collect(Collectors.toList());
        final List<SearchHits> hits = new ArrayList<>();
        mapList.forEach(stringSearchHitsMap -> {
            stringSearchHitsMap.forEach((key, value) -> hits.add(value));
        });
        Map<String, Stock> result = new HashMap<>();

        for (SearchHits sHit: hits) {
            for(org.elasticsearch.search.SearchHit hit : sHit.getHits()) {
                result.put(hit.getId(), objectMapper.readValue(hit.getSourceAsString(), Stock.class));
            }
        }
        return result;
    }

}
