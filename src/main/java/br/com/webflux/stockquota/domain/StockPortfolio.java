package br.com.webflux.stockquota.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "stock_portfolio_idx")
public class StockPortfolio {
    @Id
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private BigDecimal stockPortfolioEquity;
    private Set<StockPortfolioItem> items;

}
