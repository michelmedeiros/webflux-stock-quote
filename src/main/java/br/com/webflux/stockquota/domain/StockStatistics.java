package br.com.webflux.stockquota.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Document(indexName = "stock_statistics_idx")
public class StockStatistics {

    @Id
    private String id;

    private Long companyId;

    private String companyName;

    private String ticker;

    private BigDecimal price;

    private BigDecimal pl;

    private BigDecimal pVP;

    private BigDecimal pEbit;

    private BigDecimal pAtivo;

    private BigDecimal eVEbit;

    private BigDecimal margemBruta;

    private BigDecimal margemEbit;

    private BigDecimal margemLiquida;

    private BigDecimal pSR;

    private BigDecimal pCapitalGiro;

    private BigDecimal pAtivoCirculante;

    private BigDecimal giroAtivos;

    private BigDecimal roe;

    private BigDecimal roa;

    private BigDecimal roic;

    private BigDecimal dividaliquidaPatrimonioLiquido;

    private BigDecimal dividaLiquidaEbit;

    private BigDecimal plAtivo;

    private BigDecimal passivoAtivo;

    private BigDecimal liquidezCorrente;

    private BigDecimal pegRatio;

    private BigDecimal receitasCagr5;

    private BigDecimal lucrosCagr5;

    private BigDecimal vpa;

    private BigDecimal lpa;

    private BigDecimal valorMercado;
}
