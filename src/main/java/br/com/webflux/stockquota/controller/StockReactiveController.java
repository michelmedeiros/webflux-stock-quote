package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.service.StockQuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockReactiveController {

    private final StockQuoteService stockQuotaReactiveService;

    @PostMapping()
    public Mono<Stock> createStockQuote(@RequestBody StockDTO stockQuote) {
        return stockQuotaReactiveService.save(stockQuote);
    }

    @GetMapping("/search/client/{ticket}")
    public Mono<StockDTO> searchStockClient(@PathVariable String ticket) {
        return stockQuotaReactiveService.searchByElasticClient(ticket)
                .switchIfEmpty(monoResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    @GetMapping("/search/all")
    public Flux<Stock> searchStocks() {
        return stockQuotaReactiveService.searchAll()
                .switchIfEmpty(fluxResponseStatusNotFoundException("Stocks not found"));
    }

    @GetMapping("/search/template/{ticket}")
    public Flux<Stock> searchStock(@PathVariable String ticket) {
        return stockQuotaReactiveService.searchByTemplate(ticket)
                .switchIfEmpty(fluxResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    @GetMapping("/search/{ticket}")
    public Mono<Stock> searchStockMono(@PathVariable String ticket) {
        return stockQuotaReactiveService.getStockByTicketName(ticket)
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

    private <T> Flux<T> fluxResponseStatusNotFoundException(String message) {
        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}
