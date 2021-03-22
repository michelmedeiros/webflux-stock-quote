package br.com.webflux.stockquota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Set;

@Data
@Builder
@With
@AllArgsConstructor
public class StockPortfolioDTO {
    private String id;
    private String name;
    private Set<StockPortfolioItemDTO> items;
}
