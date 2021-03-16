package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.StockQuoteReactiveService;
import br.com.webflux.stockquota.service.YahooFinancialQuoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockQuotaReactiveController {

    private final StockQuoteReactiveService stockQuotaReactiveService;
    private final YahooFinancialQuoteService yahooFinancialQuoteService;

    @GetMapping("/search/client/{ticket}")
    public Flux<Stock> searchStockClient(@PathVariable String ticket) throws JsonProcessingException {
        return stockQuotaReactiveService.searchByElaticClient(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    @GetMapping("/search/mono/{ticket}")
    public Mono<Stock> searchStockMono(@PathVariable String ticket) {
        return stockQuotaReactiveService.getStockByTicketName(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    @GetMapping("/search/{ticket}")
    public Flux<Stock> searchStock(@PathVariable String ticket) throws JsonProcessingException {
        return stockQuotaReactiveService.searchByTemplate(ticket)
                .switchIfEmpty(fluxResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    @GetMapping("/{ticket}")
    public List<Stock> getStocks(@PathVariable String ticket) {
        return stockQuotaReactiveService.getStockQuote(ticket);
    }

    @GetMapping("yahoo/{ticket}")
    public Mono<Stock> getStock(@PathVariable String ticket) {
        return yahooFinancialQuoteService.getYahooFinanceStockQuote(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String message, Object param) {
        var messageFormat = String.format(message, param);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, messageFormat));
    }

    private <T> Flux<T> fluxResponseStatusNotFoundException(String message, Object param) {
        var messageFormat = String.format(message, param);
        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, messageFormat));
    }

    @PostMapping()
    public Mono<Stock> createStockQuote(@RequestBody Stock stockQuote) {
        return stockQuotaReactiveService.save(stockQuote);
    }

}
