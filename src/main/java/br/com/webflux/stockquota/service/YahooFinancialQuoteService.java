package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockDividend;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.domain.StockStats;
import br.com.webflux.stockquota.repository.StockQuoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import yahoofinance.YahooFinance;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class YahooFinancialQuoteService {

    private final StockQuoteRepository stockQuoteRepository;

    public Mono<Stock> getYahooFinanceStockQuote(String ticket) {
        try {
            final yahoofinance.Stock stock = YahooFinance.get(getFormattedTicket(ticket));
            if(Objects.nonNull(stock)) {
                final Stock stockQuote = convertEntity(stock);
                return this.save(stockQuote);
            }
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Mono.empty();
    }

    private String getFormattedTicket(String ticket) {
        if(Locale.getDefault().equals(this.getLocaleBrazil())) {
            return String.join(".", ticket, "SA");
        }
        return ticket;
    }

    private Locale getLocaleBrazil() {
        return new Locale("pt", "BR");
    }

    private Mono<Stock> save(Stock stockQuote) {
        return this.stockQuoteRepository.save(stockQuote);
    }

    private Stock convertEntity(yahoofinance.Stock stock) {
        return Stock.builder()
                .instant(LocalDateTime.now())
                .ticket(getFormattedSymbol(stock.getSymbol()))
                .name(stock.getName())
                .currency(stock.getCurrency())
                .stockExchange(stock.getStockExchange())
                .quote(createQuote(stock))
                .stats(createStats(stock))
                .dividend(createDividend(stock))
                .build();
    }

    private String getFormattedSymbol(String symbol) {
        return symbol.replace(".SA", "");
    }

    private StockDividend createDividend(yahoofinance.Stock stock) {
        final yahoofinance.quotes.stock.StockDividend dividend = stock.getDividend();
        return StockDividend.builder()
                .symbol(getFormattedSymbol(stock.getSymbol()))
                .payDate(dividend.getPayDate())
                .exDate(dividend.getExDate())
                .annualYield(dividend.getAnnualYield())
                .annualYieldPercent(dividend.getAnnualYieldPercent())
                .build();
    }

    private StockStats createStats(yahoofinance.Stock stock) {
        final yahoofinance.quotes.stock.StockStats stats = stock.getStats();
        return StockStats.builder()
                .symbol(getFormattedSymbol(stock.getSymbol()))
                .marketCap(stats.getMarketCap())
                .sharesFloat(stats.getSharesFloat())
                .sharesOutstanding(stats.getSharesOutstanding())
                .sharesOwned(stats.getSharesOwned())
                .eps(stats.getEps())
                .pe(stats.getPe())
                .peg(stats.getPeg())
                .epsEstimateCurrentYear(stats.getEpsEstimateCurrentYear())
                .epsEstimateNextYear(stats.getEpsEstimateNextYear())
                .epsEstimateNextQuarter(stats.getEpsEstimateNextQuarter())
                .priceBook(stats.getPriceBook())
                .priceSales(stats.getPriceSales())
                .bookValuePerShare(stats.getBookValuePerShare())
                .revenue(stats.getRevenue())
                .EBITDA(stats.getEBITDA())
                .oneYearTargetPrice(stats.getOneYearTargetPrice())
                .shortRatio(stats.getShortRatio())
                .earningsAnnouncement(stats.getEarningsAnnouncement())
                .build();
    }

    private StockQuote createQuote(yahoofinance.Stock stock) {
        final yahoofinance.quotes.stock.StockQuote quote = stock.getQuote();
        return StockQuote.builder()
                .symbol(getFormattedSymbol(stock.getSymbol()))
                .timeZone(quote.getTimeZone())
                .ask(quote.getAsk())
                .askSize(quote.getAskSize())
                .bid(quote.getBid())
                .bidSize(quote.getBidSize())
                .price(quote.getPrice())
                .lastTradeSize(quote.getLastTradeSize())
                .lastTradeDateStr(quote.getLastTradeDateStr())
                .lastTradeTimeStr(quote.getLastTradeTimeStr())
                .lastTradeTime(quote.getLastTradeTime())
                .open(quote.getOpen())
                .previousClose(quote.getPreviousClose())
                .dayLow(quote.getDayLow())
                .dayHigh(quote.getDayHigh())
                .yearHigh(quote.getYearHigh())
                .yearLow(quote.getYearLow())
                .priceAvg50(quote.getPriceAvg50())
                .priceAvg200(quote.getPriceAvg200())
                .volume(quote.getVolume())
                .avgVolume(quote.getAvgVolume())
                .build();
    }
}