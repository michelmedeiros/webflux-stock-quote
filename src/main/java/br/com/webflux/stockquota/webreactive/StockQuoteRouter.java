package br.com.webflux.stockquota.webreactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class StockQuoteRouter {

    @Bean
    public RouterFunction<ServerResponse> route(StockQuoteHandler handler) {
        return RouterFunctions.route(GET("/client/search/{ticket}")
                .and(accept(MediaType.APPLICATION_JSON)), handler::findStockQuotesByTicket)
                .andRoute(GET("/client/statistics/{ticker}")
                        .and(accept(MediaType.APPLICATION_JSON)), handler::findStockQuotesByTicker)
                .andRoute(GET("/client/stocks/generate/{ticker}")
                        .and(accept(MediaType.APPLICATION_JSON)), handler::generateStatusInvestStocks)
                .andRoute(GET("/client/yahoo/generate/{ticker}")
                        .and(accept(MediaType.APPLICATION_JSON)), handler::generateYahooStocks);

    }

}
