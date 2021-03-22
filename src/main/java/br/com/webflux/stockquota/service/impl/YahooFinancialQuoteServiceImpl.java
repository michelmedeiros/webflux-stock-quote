package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.domain.*;
import br.com.webflux.stockquota.repository.StockQuoteReactiveRepository;
import br.com.webflux.stockquota.service.YahooFinancialQuoteService;
import br.com.webflux.stockquota.utils.StockUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import yahoofinance.YahooFinance;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class YahooFinancialQuoteServiceImpl implements YahooFinancialQuoteService {

    private final StockQuoteReactiveRepository stockQuoteRepository;

    @Override
    public Mono<Stock> getYahooFinanceStockQuote(String ticker) {
        try {
            return Mono.justOrEmpty(YahooFinance.get(StockUtils.getFormattedTicket(ticker)))
                    .map(StockConverter::convertEntity)
                    .flatMap(this::save)
                    .switchIfEmpty(monoResponseStatusNotFoundException(ticker));
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(String ticker) {
        var message = String.format("Stock by ticket not found %s", ticker);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

    private Mono<Stock> save(Stock stockQuote) {
        return this.getStock(stockQuote.getTicket().toLowerCase())
                .flatMap(stock -> stockQuoteRepository.save(stockQuote.withId(stock.getId())))
                .switchIfEmpty(stockQuoteRepository.save(stockQuote));
    }


    private Mono<Stock> getStock(String ticket) {
        if(Objects.nonNull(ticket)) {
            return this.stockQuoteRepository.findFirstByTicket(ticket);
        }
        return Mono.empty();
    }
}