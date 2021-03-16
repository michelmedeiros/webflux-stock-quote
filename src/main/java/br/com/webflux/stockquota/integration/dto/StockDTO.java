package br.com.webflux.stockquota.integration.dto;

import br.com.webflux.stockquota.domain.StockQuote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class StockDTO {
    private long id;
    private String name;
    private String ticket;
    private String currency;
    private String stockExchange;
    private String normalizedName;
    private String code;
    private String price;
    private String variation;
    private StockQuoteDTO quote;

}


