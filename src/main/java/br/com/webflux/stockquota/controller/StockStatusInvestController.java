package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.service.StatusInvestStockQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/statusInvest")
@AllArgsConstructor
public class StockStatusInvestController {

    private StatusInvestStockQuoteService stockQuotaService;

    @GetMapping("/stocks/{ticker}")
    public Flux<Stock> getStocks(@PathVariable String ticker) {
        return stockQuotaService.getStatusInvestStockQuote(ticker);
    }

    @GetMapping("/statistics/generate")
    public  Flux<StockStatistics> generateStocksStatistics() {
        return stockQuotaService.generateStockStatistics();
    }

    @GetMapping("/statistics/{ticker}")
    public Mono<StockStatistics> getStocksStatisticsByTicker(@PathVariable String ticker) {
        return stockQuotaService.getStockStatisticByTicker(ticker);
    }

}
