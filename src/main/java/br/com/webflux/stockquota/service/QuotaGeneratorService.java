package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.Quote;
import br.com.webflux.stockquota.service.QuotaGeneratorService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class QuotaGeneratorService {
    public Flux<Quote> fetchQuoteStream(Duration period) {
        return null;
    }
}
