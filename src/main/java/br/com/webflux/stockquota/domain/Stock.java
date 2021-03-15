package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "stock_idx")
public class Stock {
    public static final MathContext MATH_CONTEXT = new MathContext(2);
    @Id
    private String id;
    private String ticket;
    private String name;
    private String currency;
    private String stockExchange;

    private StockQuote quote;
    private StockStats stats;
    private StockDividend dividend;

}
