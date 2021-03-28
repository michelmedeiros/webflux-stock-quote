package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.converters.StockStatisticConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.integration.dto.StockStatisticsDTO;
import br.com.webflux.stockquota.repository.StockStatisticsReactiveRepository;
import br.com.webflux.stockquota.service.StatusInvestStockService;
import br.com.webflux.stockquota.service.StockQuoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatusInvestStockQuoteServiceImpl implements StatusInvestStockService {

    public static final String SEARCH_QUERY = "{}";

    private final StatusInvestClient statusInvestClient;
    private final StockQuoteService stockQuoteService;
    private final StockStatisticsReactiveRepository stockStatisticsReactiveRepository;

    @Cacheable("statusInvest")
    private List<StockDTO> getStock(String ticket) {
        return statusInvestClient.getStock(ticket);
    }

    @Override
    public Flux<Stock> generateStockQuote(String ticker) {
        try {
            final List<StockDTO> stocks = this.getStock(ticker);
            return Flux.fromStream(stocks.stream())
                    .flatMap(stock -> stockQuoteService.getStockByTicketName(stock.getCode()))
                    .flatMap(stockFound -> stockQuoteService.save(stockFound.withId(stockFound.getId())))
                    .switchIfEmpty(stockQuoteService.saveAll(stocks));
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        }
        return null;
    }

    @Override
    public Flux<StockStatistics> generateStockStatistics() {
        final List<StockStatisticsDTO> stockStatistics = statusInvestClient.getStockStatistics(SEARCH_QUERY, 1);
        this.deleteAllStatistics();
        return stockStatisticsReactiveRepository.saveAll(StockStatisticConverter.convertEntity(stockStatistics));
    }

    @Override
    public Mono<StockStatistics> getStockStatisticByTicker(String ticker) {
        return stockStatisticsReactiveRepository.findFirstByTicker(ticker)
                .switchIfEmpty(this.monoResponseStatusNotFoundException(ticker));
    }

    public void deleteAllStatistics() {
        stockStatisticsReactiveRepository.deleteAll().subscribe();
    }

    public <T> Flux<T> fluxResponseStatusNotFoundException(){
        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Stock statistics not found"));
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(String ticker) {
        var message = String.format("Stock by ticket not found %s", ticker);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}
