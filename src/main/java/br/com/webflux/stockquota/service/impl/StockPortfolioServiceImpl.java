package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.domain.StockPortfolioItem;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import br.com.webflux.stockquota.dto.StockPortfolioItemDTO;
import br.com.webflux.stockquota.repository.StockPortfolioReactiveRepository;
import br.com.webflux.stockquota.service.StockPortfolioService;
import br.com.webflux.stockquota.utils.StockUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StockPortfolioServiceImpl implements StockPortfolioService {

    private StockPortfolioReactiveRepository stockPortfolioRepository;
    @Override
    public Mono<StockPortfolio> generate(StockPortfolioDTO stockPortfolioDTO) {
        try {
            this.processItems(stockPortfolioDTO);
            return stockPortfolioRepository.save(createPortfolio(stockPortfolioDTO));
        } catch (Exception ex) {
            log.error("Error to generated stock portfolio", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void processItems(StockPortfolioDTO stockPortfolioDTO) {
        stockPortfolioDTO.getItems().forEach(item -> {
            var stock = this.getStock(item.getTicket());
            item.setLastPrice(stock.getQuote().getPrice());
            item.setCurrentPosition(item.getLastPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setPurchasePrice(item.getAveragePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setProfitability(item.getCurrentPosition().subtract(item.getPurchasePrice()));
            item.setProfitabilityPercentage(item.getProfitability().doubleValue() /
                    item.getPurchasePrice().doubleValue());
        });
    }

    private StockPortfolio createPortfolio(StockPortfolioDTO stockPortfolioDTO) {
        return StockPortfolio.builder()
                .createdAt(LocalDateTime.now())
                .name(stockPortfolioDTO.getName())
                .items(createItemsPortfolio(stockPortfolioDTO.getItems()))
                .build();
    }

    private Set<StockPortfolioItem> createItemsPortfolio(Set<StockPortfolioItemDTO> items) {
        return items.stream().map(this::convertEntity)
                .collect(Collectors.toSet());
    }

    private StockPortfolioItem convertEntity(StockPortfolioItemDTO item) {
        return StockPortfolioItem.builder()
                .ticket(item.getTicket())
                .averagePrice(item.getAveragePrice())
                .currentPosition(item.getCurrentPosition())
                .lastPrice(item.getLastPrice())
                .profitability(item.getProfitability())
                .purchasePrice(item.getPurchasePrice())
                .quantity(item.getQuantity())
                .build();
    }

    private Stock getStock(String ticket) {
        try {
            final yahoofinance.Stock stock = YahooFinance.get(StockUtils.getFormattedTicket(ticket));
            if(Objects.nonNull(stock)) {
                return StockConverter.convertEntity(stock);
            }
        } catch (Exception ex) {
            log.error("Error to call Yahoo Finance", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Stock.builder().build();
    }
}
