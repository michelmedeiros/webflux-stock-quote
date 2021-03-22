package br.com.webflux.stockquota.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Document(indexName = "stock_portfolio_idx")
public class StockPortfolio {
    @Id
    private String id;
    private String name;
    private Date createdAt;
    private Date updateAt;
    private BigDecimal stockPortfolioEquity;
    private Set<StockPortfolioItem> items;

}
