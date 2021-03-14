package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockQuotaDTO;
import br.com.webflux.stockquota.repository.StockQuoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class StockQuotaService {

    @Autowired
    private final StatusInvestClient statusInvestClient;
    @Autowired
    private final StockQuoteRepository stockQuoteRepository;

    public List<Stock> getStockQuote(String ticket) {
        try {
            final List<StockQuotaDTO> stocks = statusInvestClient.getStock(ticket);
            return stocks.stream().map(this::convertEntity)
                            .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        }
        return null;
    }

    private Stock convertEntity(StockQuotaDTO stock) {
        return Stock.builder()
                .instant(LocalDateTime.now())
                .quote(StockQuote.builder()
                        .price(new BigDecimal(stock.getPrice().replaceAll(",", ".")))
                        .build())
                .ticket(stock.getCode())
                .build();
    }

    public Mono<Stock> save(Stock stockQuote) {
        return stockQuoteRepository.save(stockQuote);
    }
}
