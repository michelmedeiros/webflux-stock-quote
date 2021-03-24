package br.com.webflux.stockquota.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockStatisticsDTO {

    private Long companyId;

    private String companyName;

    private String ticker;

    private BigDecimal price;

    @JsonProperty(value = "p_L")
    private BigDecimal pl;

    @JsonProperty(value = "p_VP")
    private BigDecimal pVP;

    @JsonProperty(value = "p_Ebit")
    private BigDecimal pEbit;

    @JsonProperty(value = "p_Ativo")
    private BigDecimal pAtivo;

    @JsonProperty(value = "eV_Ebit")
    private BigDecimal eVEbit;

    private BigDecimal margemBruta;

    private BigDecimal margemEbit;

    private BigDecimal margemLiquida;

    @JsonProperty(value = "p_SR")
    private BigDecimal pSR;

    @JsonProperty(value = "p_CapitalGiro")
    private BigDecimal pCapitalGiro;

    @JsonProperty(value = "p_AtivoCirculante")
    private BigDecimal pAtivoCirculante;

    private BigDecimal giroAtivos;

    private BigDecimal roe;

    private BigDecimal roa;

    private BigDecimal roic;

    private BigDecimal dividaliquidaPatrimonioLiquido;

    private BigDecimal dividaLiquidaEbit;

    @JsonProperty(value = "pl_Ativo")
    private BigDecimal plAtivo;

    @JsonProperty(value = "passivo_Ativo")
    private BigDecimal passivoAtivo;

    private BigDecimal liquidezCorrente;

    @JsonProperty(value = "peg_Ratio")
    private BigDecimal pegRatio;

    @JsonProperty(value = "receitas_Cagr5")
    private BigDecimal receitasCagr5;

    @JsonProperty(value = "lucros_Cagr5")
    private BigDecimal lucrosCagr5;

    private BigDecimal vpa;

    private BigDecimal lpa;

    private BigDecimal valorMercado;

}

