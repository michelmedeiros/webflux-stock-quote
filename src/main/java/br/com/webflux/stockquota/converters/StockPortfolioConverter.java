package br.com.webflux.stockquota.converters;

import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.domain.StockPortfolioItem;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import br.com.webflux.stockquota.dto.StockPortfolioItemDTO;
import org.springframework.cglib.core.Local;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class StockPortfolioConverter {

    public static StockPortfolio convertEntity(StockPortfolioDTO stockPortfolioDTO) {
        return StockPortfolio.builder()
                .createdAt(new Date())
                .name(stockPortfolioDTO.getName())
                .items(createItemsPortfolio(stockPortfolioDTO.getItems()))
                .build();
    }

    public static Mono<StockPortfolio> convertEntityMono(StockPortfolioDTO stockPortfolioDTO) {
        return Mono.just(convertEntity(stockPortfolioDTO));
    }

    private static Set<StockPortfolioItem> createItemsPortfolio(Set<StockPortfolioItemDTO> items) {
        return items.stream().map(StockPortfolioConverter::convertItemEntity)
                .collect(Collectors.toSet());
    }

    private static StockPortfolioItem convertItemEntity(StockPortfolioItemDTO item) {
        return StockPortfolioItem.builder()
                .ticket(item.getTicket())
                .averagePrice(item.getAveragePrice())
                .currentPosition(item.getCurrentPosition())
                .lastPrice(item.getLastPrice())
                .profitability(item.getProfitability())
                .purchasePrice(item.getPurchasePrice())
                .quantity(item.getQuantity())
                .profitabilityPercentage(item.getProfitabilityPercentage())
                .build();
    }

    public static StockPortfolio converterDTOEntity(StockPortfolioDTO stockPortfolioDTO, StockPortfolio stockPortfolio) {
        return StockPortfolio.builder().build();
    }

}
