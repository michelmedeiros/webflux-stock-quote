package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.StockQuotaService;
import br.com.webflux.stockquota.service.YahooFinancialQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockQuotaController {

    private StockQuotaService stockQuotaService;
    private YahooFinancialQuoteService yahooFinancialQuoteService;

//    public StockQuotaController(StockQuotaService stockQuotaService) {
//        this.stockQuotaService = stockQuotaService;
//    }

    @GetMapping("/{ticket}")
    public List<Stock> getMovie(@PathVariable String ticket) {
        return stockQuotaService.getStockQuote(ticket);
    }

    @GetMapping("yahoo/{ticket}")
    public Mono<Stock> getMovieYahoo(@PathVariable String ticket) {
        return yahooFinancialQuoteService.getYahooFinanceStockQuote(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String message, Object param) {
        var messageFormat = String.format(message, param);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, messageFormat));
    }

    @PostMapping()
    public Mono<Stock> createStockQuote(@RequestBody Stock stockQuote) {
        return stockQuotaService.save(stockQuote);
    }

}
