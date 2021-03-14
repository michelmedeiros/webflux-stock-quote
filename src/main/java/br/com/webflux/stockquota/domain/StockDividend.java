package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "stock_dividend_idx")
public class StockDividend {
    private String symbol;
    private Calendar payDate;
    private Calendar exDate;
    private BigDecimal annualYield;
    private BigDecimal annualYieldPercent;
}
