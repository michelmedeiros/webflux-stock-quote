package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Stock;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class QuotaGeneratorService {
    public Flux<Stock> fetchQuoteStream(Duration period) {
        return null;
    }
}
