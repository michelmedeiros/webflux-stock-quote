package br.com.webflux.stockquota.converters;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockDividend;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.domain.StockStats;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.integration.dto.StockQuoteDTO;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StockConverter {

    public static Stock convertEntity(StockDTO stock) {
        return Stock.builder()
                .ticket(stock.getCode())
                .name(stock.getName())
                .price(convertNumber(stock.getPrice()))
                .variation(convertNumber(stock.getVariation()))
                .currency(stock.getCurrency())
                .stockExchange(stock.getStockExchange())
                .quote(createQuote(stock.getQuote()))
                .build();
    }

    private static BigDecimal convertNumber(String value) {
        if(Objects.nonNull(value)) {
            final String replaceSymbol = value.replace(",", ".");
            return new BigDecimal(replaceSymbol);
        }
        return BigDecimal.ZERO;
    }

    private static StockQuote createQuote(StockQuoteDTO quote) {
        return Objects.nonNull(quote) ?
                StockQuote.builder()
                .symbol(getFormattedSymbol(quote.getSymbol()))
                .ask(quote.getAsk())
                .askSize(quote.getAskSize())
                .bid(quote.getBid())
                .bidSize(quote.getBidSize())
                .price(quote.getPrice())
                .lastTradeSize(quote.getLastTradeSize())
                .lastTradeDateStr(quote.getLastTradeDateStr())
                .lastTradeTimeStr(quote.getLastTradeTimeStr())
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
                .build() : null;
    }

    public static Stock convertEntity(yahoofinance.Stock stock) {
        return Stock.builder()
                .ticket(getFormattedSymbol(stock.getSymbol()))
                .name(stock.getName())
                .currency(stock.getCurrency())
                .stockExchange(stock.getStockExchange())
                .quote(createQuote(stock))
                .stats(createStats(stock))
                .dividend(createDividend(stock))
                .build();
    }

    private static String getFormattedSymbol(String symbol) {
        return Objects.nonNull(symbol) ? symbol.replace(".SA", "") : null;
    }

    private static StockDividend createDividend(yahoofinance.Stock stock) {
        final yahoofinance.quotes.stock.StockDividend dividend = stock.getDividend();
        return StockDividend.builder()
                .symbol(getFormattedSymbol(stock.getSymbol()))
                .payDate(dividend.getPayDate())
                .exDate(dividend.getExDate())
                .annualYield(dividend.getAnnualYield())
                .annualYieldPercent(dividend.getAnnualYieldPercent())
                .build();
    }

    private static StockStats createStats(yahoofinance.Stock stock) {
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

    private static StockQuote createQuote(yahoofinance.Stock stock) {
        final yahoofinance.quotes.stock.StockQuote quote = stock.getQuote();
        return StockQuote.builder()
                .symbol(getFormattedSymbol(stock.getSymbol()))
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

    public static List<Stock> convertEntities(List<StockDTO> stockQuoteList) {
        return stockQuoteList.stream().map(StockConverter::convertEntity).collect(Collectors.toList());
    }
}
