package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.repository.StockQuoteReactiveRepository;
import br.com.webflux.stockquota.service.StockQuoteReactiveService;
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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class StockQuoteReactiveServiceImpl implements StockQuoteReactiveService {

    private final StockQuoteReactiveRepository stockQuoteRepository;
    private final ReactiveElasticsearchClient client;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Stock> getStockByTicketName(String ticket) {
        return stockQuoteRepository.findFirstByTicket(ticket);
    }



    @Override
    public Mono<Stock> save(Stock stockQuote) {
        return stockQuoteRepository.save(stockQuote);
    }

    @Override
    public Flux<Stock> searchByElasticClient(String ticket) {
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("ticket", ticket.toUpperCase()));
        final SearchRequest searchRequest = new SearchRequest().indices("stock_idx")
                .source(searchSourceBuilder);

        final Flux<org.elasticsearch.search.SearchHit> searchHits = client.search(searchRequest);

        try {
            final Map<String, Stock> stringStockMap = parseHits(searchHits);
        }catch (JsonProcessingException ex) {
            log.error("Error processing data", ex);
        }

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
