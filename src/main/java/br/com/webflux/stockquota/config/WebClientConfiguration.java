package br.com.webflux.stockquota.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final int TIMEOUT = 1000;

    @Value("${status-invest.url}")
    private String url;

//    @Bean
//    public WebClient webClientStatusInvest() {
//        HttpClient httpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
//                .doOnConnected(conn -> conn
//                        .addHandlerLast(new ReadTimeoutHandler(10))
//                        .addHandlerLast(new WriteTimeoutHandler(10)));
//
//        return WebClient.builder()
//                .baseUrl(url)
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//    }

    @Bean
    public WebClient webClientJsonPlaceHolder() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10))
                        .addHandlerLast(new WriteTimeoutHandler(10)));

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}