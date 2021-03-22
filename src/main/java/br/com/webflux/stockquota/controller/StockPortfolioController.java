package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import br.com.webflux.stockquota.service.StockPortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/portfolios")
@AllArgsConstructor
public class StockPortfolioController {

    private final StockPortfolioService stockPortfolioService;

    @PostMapping("/generate")
    public Mono<StockPortfolio> saveStockPortfolio(@RequestBody StockPortfolioDTO stockPortfolioDTO) {
        return stockPortfolioService.generate(stockPortfolioDTO);
    }

    @PutMapping("/synchronize")
    public Mono<StockPortfolio> synchronizeStockPortfolio(@RequestBody StockPortfolioDTO stockPortfolioDTO) {
        return stockPortfolioService.synchronize(stockPortfolioDTO);
    }
}
