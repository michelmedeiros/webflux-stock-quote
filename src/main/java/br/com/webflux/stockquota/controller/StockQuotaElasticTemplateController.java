package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.StockQuoteElasticTemplateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stocks/template")
@AllArgsConstructor
public class StockQuotaElasticTemplateController {

    private StockQuoteElasticTemplateService stockQuotaReactiveService;

    @GetMapping("/search/{ticket}")
    public Flux<Stock> searchStock(@PathVariable String ticket) throws JsonProcessingException {
        return stockQuotaReactiveService.searchByTemplate(ticket)
                .switchIfEmpty(fluxResponseStatusNotFoundException("Stock by ticket not found %s", ticket));
    }

    private <T> Flux<T> fluxResponseStatusNotFoundException(String message, Object param) {
        var messageFormat = String.format(message, param);
        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, messageFormat));
    }
}
