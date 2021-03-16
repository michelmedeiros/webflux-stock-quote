package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockQuote;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.integration.dto.StockQuotaDTO;
import br.com.webflux.stockquota.service.StockQuoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StockQuoteServiceImpl implements StockQuoteService {

    private final StatusInvestClient statusInvestClient;
    @Override
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
                .quote(StockQuote.builder()
                        .price(new BigDecimal(stock.getPrice().replaceAll(",", ".")))
                        .build())
                .ticket(stock.getCode())
                .build();
    }
}
