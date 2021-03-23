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

import java.io.IOException;
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
            log.info("Starting execution search by ticker {}", ticker);
            return Mono.justOrEmpty(getYahooStock(ticker))
                    .map(StockConverter::convertEntity)
                    .flatMap(this::save)
                    .switchIfEmpty(monoResponseStatusNotFoundException(ticker));
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } finally {
            log.info("Finished save stock in ES");
        }
    }

    @Override
    public Mono<Stock> getYahooFinanceStockQuoteNonReactive(String ticker) {
        try {
            log.info("Starting execution search by ticker {}", ticker);
            final Stock stock = StockConverter.convertEntity(getYahooStock(ticker));
            return save(stock)
                    .switchIfEmpty(monoResponseStatusNotFoundException(ticker));
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } finally {
            log.info("Finished save stock in ES");
        }
    }

    private yahoofinance.Stock getYahooStock(String ticker) throws IOException {
        log.info("Starting find stock in Yahoo Finance by ticker {}", ticker);
        yahoofinance.Stock stock = YahooFinance.get(StockUtils.getFormattedTicket(ticker));
        log.info("Finished find stock in Yahoo Finance");
        return stock;
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(String ticker) {
        var message = String.format("Stock by ticket not found %s", ticker);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

    private Mono<Stock> save(Stock stockQuote) {
        log.info("Starting save stock in ES");
        Mono<Stock> stockMono = this.getStock(stockQuote.getTicket().toLowerCase())
                .flatMap(stock -> stockQuoteRepository.save(stockQuote.withId(stock.getId())))
                .switchIfEmpty(stockQuoteRepository.save(stockQuote));
        log.info("Finished save stock in ES");
        return stockMono;
    }


    private Mono<Stock> getStock(String ticket) {
        log.info("Starting find stock in ES");
        if(Objects.nonNull(ticket)) {
            Mono<Stock> firstByTicket = this.stockQuoteRepository.findFirstByTicket(ticket);
            log.info("Finished find stock in ES");
            return firstByTicket;
        }
        return Mono.empty();
    }
}