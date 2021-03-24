package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.service.StatusInvestStockQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockQuotaController {

    private StatusInvestStockQuoteService stockQuotaService;

    @GetMapping("/statusInvest/{ticket}")
    public Flux<Stock> getStocks(@PathVariable String ticket) {
        return stockQuotaService.getStatusInvestStockQuote(ticket);
    }

    @GetMapping("/statusInvest/statistics")
    public  Flux<StockStatistics> generateStocksStatistics() {
        return stockQuotaService.generateStockStatistics();
    }

}
