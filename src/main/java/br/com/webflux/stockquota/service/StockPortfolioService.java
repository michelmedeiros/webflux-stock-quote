package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.StockPortfolio;
import br.com.webflux.stockquota.dto.StockPortfolioDTO;
import reactor.core.publisher.Mono;

public interface StockPortfolioService {
    Mono<StockPortfolio> generate(StockPortfolioDTO stockPortifolioDTO);
}
