package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

public interface StatusInvestWebClientService {
    Flux<Stock> getStocks(@RequestParam("ticker") String query);
}
