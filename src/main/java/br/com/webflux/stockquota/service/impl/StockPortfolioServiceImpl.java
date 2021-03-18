package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.converters.StockConverter;
import br.com.webflux.stockquota.converters.StockPortfolioConverter;
import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.repository.StockPortfolioReactiveRepository;
import br.com.webflux.stockquota.service.StockPortfolioService;
import br.com.webflux.stockquota.utils.StockUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class StockPortfolioServiceImpl implements StockPortfolioService {

    private StockPortfolioReactiveRepository stockPortfolioRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<StockPortfolio> generate(StockPortfolioDTO stockPortfolioDTO) {
        try {
            final StockPortfolio stockPortfolio = this.processItems(stockPortfolioDTO);
            return stockPortfolioRepository.save(stockPortfolio);
        } catch (Exception ex) {
            log.error("Error to generated stock portfolio", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Mono<StockPortfolio> synchronize(StockPortfolioDTO stockPortfolioDTO) {
        final StockPortfolio stockPortfolioProcess = this.processItems(stockPortfolioDTO);
        stockPortfolioProcess.setUpdateAt(new Date());
        return this.getStockPortfolio(stockPortfolioDTO.getId())
                .map(stockPortfolioFound -> stockPortfolioProcess.withId(stockPortfolioFound.getId()))
                .flatMap(stockPortfolioRepository::save)
                .thenReturn(stockPortfolioProcess);
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(){
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"StockPortfolio not found"));
    }

    private Mono<StockPortfolio> getStockPortfolio(String id) {
        if(Objects.nonNull(id)) {
            return this.stockPortfolioRepository.findById(id).switchIfEmpty(monoResponseStatusNotFoundException());
        }
        return Mono.empty();
    }

    private StockPortfolio processItems(StockPortfolioDTO stockPortfolioDTO) {
        stockPortfolioDTO.getItems().forEach(item -> {
            var stock = this.getStock(item.getTicket());
            item.setLastPrice(stock.getQuote().getPrice());
            item.setCurrentPosition(item.getLastPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setPurchasePrice(item.getAveragePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setProfitability(item.getCurrentPosition().subtract(item.getPurchasePrice()));
            item.setProfitabilityPercentage(item.getProfitability().doubleValue() /
                    item.getPurchasePrice().doubleValue());
        });
        return StockPortfolioConverter.convertEntity(stockPortfolioDTO);
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
