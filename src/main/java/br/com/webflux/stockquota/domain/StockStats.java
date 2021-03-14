package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "stock_stats_idx")
public class StockStats {
    private String symbol;
    private BigDecimal marketCap;
    private Long sharesFloat;
    private Long sharesOutstanding;
    private Long sharesOwned;

    private BigDecimal eps;
    private BigDecimal pe;
    private BigDecimal peg;

    private BigDecimal epsEstimateCurrentYear;
    private BigDecimal epsEstimateNextQuarter;
    private BigDecimal epsEstimateNextYear;

    private BigDecimal priceBook;
    private BigDecimal priceSales;
    private BigDecimal bookValuePerShare;

    private BigDecimal revenue; // ttm
    private BigDecimal EBITDA; // ttm
    private BigDecimal oneYearTargetPrice;

    private BigDecimal shortRatio;

    private Calendar earningsAnnouncement;
}
