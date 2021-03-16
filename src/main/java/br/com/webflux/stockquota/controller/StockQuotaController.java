package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.StockQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockQuotaController {

    private StockQuoteService stockQuotaService;

    @GetMapping("/{ticket}")
    public List<Stock> getStocks(@PathVariable String ticket) {
        return stockQuotaService.getStockQuote(ticket);
    }

}
