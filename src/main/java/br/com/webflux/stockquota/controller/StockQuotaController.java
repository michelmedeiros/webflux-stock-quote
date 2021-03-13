package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Quote;
import br.com.webflux.stockquota.service.StockQuotaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockQuotaController {

    private StockQuotaService stockQuotaService;

    public StockQuotaController(StockQuotaService stockQuotaService) {
        this.stockQuotaService = stockQuotaService;
    }

    @GetMapping("/{ticket}")
    public List<Quote> getMovie(@PathVariable String ticket) {
        return stockQuotaService.getStockQuote(ticket);
    }

}
