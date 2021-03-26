package br.com.webflux.stockquota.repository;

import br.com.webflux.stockquota.domain.StockStatistics;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockStatisticsReactiveRepository extends ReactiveCrudRepository<StockStatistics, String> {
    Mono<StockStatistics> findFirstByTicker(String ticker);
}
