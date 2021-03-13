package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Quote;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockQuotaDTO;
import br.com.webflux.stockquota.service.StockQuotaService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class StockQuotaService {

    private StatusInvestClient statusInvestClient;
    public List<Quote> getStockQuote(String ticket) {
        try {
            final List<StockQuotaDTO> stocks = statusInvestClient.getStock(ticket);
            return stocks.stream().map(this::convertEntity)
                            .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        }
        return null;
    }

    private Quote convertEntity(StockQuotaDTO stock) {
        return Quote.builder()
                .instant(Instant.now())
                .price(new BigDecimal(stock.getPrice().replaceAll(",", ".")))
                .ticket(stock.getCode())
                .build();
    }
}
