package br.com.webflux.stockquota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;

@Order(1)
@SpringBootApplication
public class StockQuotaApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockQuotaApplication.class, args);
	}
}
