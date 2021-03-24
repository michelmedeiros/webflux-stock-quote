package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.converters.StockStatisticConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.integration.dto.StockStatisticsDTO;
import br.com.webflux.stockquota.service.StatusInvestStockQuoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@Service
@AllArgsConstructor
public class StatusInvestStockQuoteServiceImpl implements StatusInvestStockQuoteService {

    public static final String SEARCH_QUERY = "{}";

    private final StatusInvestClient statusInvestClient;
    private final ReactiveElasticsearchOperations elasticsearchTemplate;

    @Override
    public Flux<Stock> getStatusInvestStockQuote(String ticket) {
        try {
            final List<StockDTO> stocks = statusInvestClient.getStock(ticket);
            return Flux.fromStream(stocks.stream().map(StockConverter::convertEntity))
                    .switchIfEmpty(this.fluxResponseStatusNotFoundException());
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        }
        return null;
    }

    @Override
    public Flux<StockStatistics> generateStockStatistics() {
        final List<StockStatisticsDTO> stockStatistics = statusInvestClient.getStockStatistics(SEARCH_QUERY, 1);
        return this.saveAllStatistics(StockStatisticConverter.convertEntity(stockStatistics));
    }

    public Flux<StockStatistics> saveAllStatistics(List<StockStatistics> statistics) {
        statistics.forEach(stockStatistics -> {
            final Flux<StockStatistics> stockStatisticsFlux =
                    this.searchByTemplate(stockStatistics.getTicker())
                            .flatMap(stockPortfolioFound ->  update(stockPortfolioFound, stockStatistics))
                            .switchIfEmpty(save(stockStatistics));

//                this.searchByTemplate(stockStatistics.getTicker())
//                        .map(stockPortfolioFound -> stockStatistics.withId(stockPortfolioFound.getId()))
//                        .flatMap(this::saveOrUpdte)
//                        .switchIfEmpty(saveOrUpdte(stockStatistics));
        });

        return Flux.fromStream(statistics.stream());

    }

    private Mono<StockStatistics> save(StockStatistics stockStatistics) {
        return  elasticsearchTemplate.save(stockStatistics, IndexCoordinates.of("stock_statistics_idx"));
    }
    private Mono<StockStatistics> update(StockStatistics stockStatisticsFound, StockStatistics stockStatistics) {
        if(Objects.nonNull(stockStatisticsFound.getId())) {
            final StockStatistics stockStatisticsUpdate = stockStatistics.withId(stockStatisticsFound.getId());
            return elasticsearchTemplate.save(stockStatisticsUpdate, IndexCoordinates.of("stock_statistics_idx"));
        }
        return Mono.empty();
    }

    public Flux<StockStatistics> searchByTemplate(String ticker) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("ticket", ticker.toUpperCase()))
                .build();
        final Flux<SearchHit<StockStatistics>> stockFlux = elasticsearchTemplate
                .search(searchQuery, StockStatistics.class, IndexCoordinates.of("stock_statistics_idx"));
        return stockFlux.map(SearchHit::getContent);
    }

//    public Mono<MyModel> saveMyModel(MyModel myModel){
//
//        return reactiveElasticsearchOperations.save(
//                myModel,
//                IndexCoordinates.of(MYMODEL_ES_INDEX)
//        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
//    }


    public <T> Flux<T> fluxResponseStatusNotFoundException(){
        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Stock statistics not found"));
    }

}
