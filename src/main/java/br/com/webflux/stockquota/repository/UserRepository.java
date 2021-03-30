package br.com.webflux.stockquota.repository;

import br.com.webflux.stockquota.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
}
