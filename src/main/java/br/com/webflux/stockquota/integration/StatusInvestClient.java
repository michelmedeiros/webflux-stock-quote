package br.com.webflux.stockquota.integration;

import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.integration.dto.StockQuoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "statusInvestClient", url = "${status-invest.url}")
public interface StatusInvestClient {
    @GetMapping("/home/mainsearchquery")
    List<StockDTO> getStock(@RequestParam("q") String query);
}
