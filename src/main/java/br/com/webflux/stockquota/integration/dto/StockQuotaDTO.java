package br.com.webflux.stockquota.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class StockQuotaDTO {
    private long id;
    private String nameFormated;
    private String name;
    private String normalizedName;
    private String code;
    private String price;
    private String variation;
}


