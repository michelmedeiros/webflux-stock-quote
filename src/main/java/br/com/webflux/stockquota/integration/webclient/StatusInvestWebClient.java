package br.com.webflux.stockquota.integration.webclient;

import br.com.webflux.stockquota.integration.dto.StockDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@AllArgsConstructor
public class StatusInvestWebClient {
    private static final String URL_QUERY = "/home/mainsearchquery";
    private final WebClient webClient;

    public Flux<StockDTO> getStocks(final String query) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL_QUERY)
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .bodyToFlux(StockDTO.class);
//                .delayElements(Duration.ofSeconds(10));
    }

}
