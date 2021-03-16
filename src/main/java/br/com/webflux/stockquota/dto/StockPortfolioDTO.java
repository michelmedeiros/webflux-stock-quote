package br.com.webflux.stockquota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class StockPortfolioDTO {
    private String name;
    private Set<StockPortfolioItemDTO> items;
}
