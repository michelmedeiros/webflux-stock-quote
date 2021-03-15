package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes2.HistoricalDividend;
import yahoofinance.histquotes2.HistoricalSplit;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockStats;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "stock_quote_idx")
public class StockQuote {

    private String symbol;
    private BigDecimal ask;
    private Long askSize;
    private BigDecimal bid;
    private Long bidSize;
    private BigDecimal price;

    private Long lastTradeSize;
    private String lastTradeDateStr;
    private String lastTradeTimeStr;
    private Calendar lastTradeTime;

    private BigDecimal open;
    private BigDecimal previousClose;
    private BigDecimal dayLow;
    private BigDecimal dayHigh;

    private BigDecimal yearLow;
    private BigDecimal yearHigh;
    private BigDecimal priceAvg50;
    private BigDecimal priceAvg200;

    private Long volume;
    private Long avgVolume;
}
