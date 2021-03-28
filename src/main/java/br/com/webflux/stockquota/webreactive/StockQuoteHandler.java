package br.com.webflux.stockquota.webreactive;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.domain.StockStatistics;
import br.com.webflux.stockquota.service.StatusInvestStockService;
import br.com.webflux.stockquota.service.StockQuoteService;
import br.com.webflux.stockquota.service.impl.StockQuoteReactiveServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class StockQuoteHandler {
    private final StockQuoteService stockQuoteReactiveService;
    private final StatusInvestStockService stockStatisticsReactiveService;
    private final StatusInvestStockService statusInvestStockService;

    public Mono<ServerResponse> generateStocks(ServerRequest request) {
        String ticket = request.pathVariable("ticket");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.statusInvestStockService.generateStockQuote(ticket), Stock.class)
                .switchIfEmpty(ServerResponse.notFound().build());

    }
    public Mono<ServerResponse> findStockQuotesByTicket(ServerRequest request) {
        String ticket = request.pathVariable("ticket");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.stockQuoteReactiveService.getStockByTicketName(ticket), Stock.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findStockQuotesByTicker(ServerRequest request) {
        String ticker = request.pathVariable("ticker");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.stockStatisticsReactiveService.getStockStatisticByTicker(ticker), StockStatistics.class)
                .switchIfEmpty(ServerResponse.notFound().build());
//                .timeout(Duration.ofSeconds(60));
    }

}
