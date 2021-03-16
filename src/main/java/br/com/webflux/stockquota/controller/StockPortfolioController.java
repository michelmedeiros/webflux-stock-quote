package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import br.com.webflux.stockquota.service.StockPortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/portfolios")
@AllArgsConstructor
public class StockPortfolioController {

    private final StockPortfolioService stockPortfolioService;

    @PostMapping
    public Mono<StockPortfolio> saveStockPortfolio(@RequestBody StockPortfolioDTO stockPortfolioDTO) {
        return stockPortfolioService.generate(stockPortfolioDTO);
    }
}
