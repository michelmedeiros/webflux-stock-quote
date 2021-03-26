package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.impl.YahooFinancialQuoteServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/yahoo")
@AllArgsConstructor
public class StockYahooController {

    private final YahooFinancialQuoteServiceImpl yahooFinancialQuoteService;

    @GetMapping("/search/{ticket}")
    public Mono<Stock> getStock(@PathVariable String ticket) {
        return yahooFinancialQuoteService.getYahooFinanceStockQuote(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String message, Object param) {
        var messageFormat = String.format(message, param);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, messageFormat));
    }
}
