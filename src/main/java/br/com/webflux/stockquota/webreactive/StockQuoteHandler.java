package br.com.webflux.stockquota.webreactive;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.service.impl.StockQuoteReactiveServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class StockQuoteHandler {
    private final StockQuoteReactiveServiceImpl stockQuoteReactiveService;

    public Mono<ServerResponse> findStockQuotesByTicket(ServerRequest request) {
        String ticket = request.pathVariable("ticket");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.stockQuoteReactiveService.getStockByTicketName(ticket), Stock.class);
    }
}
