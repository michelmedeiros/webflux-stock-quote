package br.com.webflux.stockquota.config;

import br.com.webflux.stockquota.integration.feign.StatusInvestClient;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = StatusInvestClient.class)
public class FeignConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

}