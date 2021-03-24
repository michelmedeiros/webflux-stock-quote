package br.com.webflux.stockquota.converters;

import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.integration.dto.StockStatisticsDTO;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockStatisticConverter {
    public static List<StockStatistics> convertEntity(List<StockStatisticsDTO> stockStatistics) {
        return stockStatistics.stream().map(StockStatisticConverter::convertItemEntity).collect(Collectors.toList());
    }

    private static StockStatistics convertItemEntity(StockStatisticsDTO statisticsDTO) {
        return StockStatistics.builder()
                .companyId(statisticsDTO.getCompanyId())
                .companyName(statisticsDTO.getCompanyName())
                .ticker(statisticsDTO.getTicker())
                .price(statisticsDTO.getPrice())
                .pl(statisticsDTO.getPl())
                .pVP(statisticsDTO.getPVP())
                .pEbit(statisticsDTO.getPEbit())
                .pAtivo(statisticsDTO.getPAtivo())
                .eVEbit(statisticsDTO.getEVEbit())
                .margemBruta(statisticsDTO.getMargemBruta())
                .margemEbit(statisticsDTO.getMargemEbit())
                .margemLiquida(statisticsDTO.getMargemLiquida())
                .pSR(statisticsDTO.getPSR())
                .pCapitalGiro(statisticsDTO.getPCapitalGiro())
                .pAtivoCirculante(statisticsDTO.getPAtivoCirculante())
                .giroAtivos(statisticsDTO.getGiroAtivos())
                .roe(statisticsDTO.getRoe())
                .roa(statisticsDTO.getRoa())
                .roic(statisticsDTO.getRoic())
                .dividaliquidaPatrimonioLiquido(statisticsDTO.getDividaliquidaPatrimonioLiquido())
                .dividaLiquidaEbit(statisticsDTO.getDividaLiquidaEbit())
                .plAtivo(statisticsDTO.getPlAtivo())
                .passivoAtivo((statisticsDTO.getPassivoAtivo()))
                .liquidezCorrente(statisticsDTO.getLiquidezCorrente())
                .pegRatio(statisticsDTO.getPegRatio())
                .receitasCagr5(statisticsDTO.getReceitasCagr5())
                .lucrosCagr5(statisticsDTO.getLucrosCagr5())
                .vpa(statisticsDTO.getVpa())
                .lpa(statisticsDTO.getLpa())
                .valorMercado(statisticsDTO.getValorMercado())
                .build();

    }
}
