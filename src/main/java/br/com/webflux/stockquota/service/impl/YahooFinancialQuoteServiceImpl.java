package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockDividend;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.domain.StockStats;
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
    public Mono<Stock> getYahooFinanceStockQuote(String ticket) {
        try {
            final yahoofinance.Stock stock = YahooFinance.get(StockUtils.getFormattedTicket(ticket));
            if(Objects.nonNull(stock)) {
                final Stock stockQuote = StockConverter.convertEntity(stock);
                return this.save(stockQuote);
            }
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Mono.empty();
    }

    private Mono<Stock> save(Stock stockQuote) {
        this.deleteExistingStock(stockQuote);
        return stockQuoteRepository.save(stockQuote);
    }

    private void deleteExistingStock(Stock stockQuote) {
        this.stockQuoteRepository
                .findFirstByTicket(stockQuote.getTicket().toLowerCase())
                .map(stockQuoteRepository::delete);
    }
}