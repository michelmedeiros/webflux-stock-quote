package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.service.StatusInvestStockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/statusInvest")
@AllArgsConstructor
public class StockStatusInvestController {

    private StatusInvestStockService stockQuotaService;

    @GetMapping("/stocks/generate/{ticker}")
    public Flux<Stock> generateStocks(@PathVariable String ticker) {
        return stockQuotaService.generateStockQuote(ticker);
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
