package br.com.webflux.stockquota.repository;

import br.com.webflux.stockquota.domain.StockPortfolio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockPortfolioReactiveRepository extends ReactiveCrudRepository<StockPortfolio, String> {
}