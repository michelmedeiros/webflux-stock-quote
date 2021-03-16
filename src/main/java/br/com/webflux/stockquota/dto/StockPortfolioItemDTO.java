package br.com.webflux.stockquota.dto;

import br.com.webflux.stockquota.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class StockPortfolioItemDTO {
    private Integer quantity;
    private BigDecimal averagePrice;
    private BigDecimal purchasePrice;
    private BigDecimal lastPrice;
    private BigDecimal currentPosition;
    private BigDecimal profitability;
    private Double profitabilityPercentage;
    private String ticket;
}
