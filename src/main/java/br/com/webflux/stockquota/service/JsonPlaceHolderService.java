package br.com.webflux.stockquota.service;

import br.com.webflux.stockquota.domain.User;
import reactor.core.publisher.Flux;

public interface JsonPlaceHolderService {
    Flux<User> getUsers();
    Flux<User> generateUsers();
}
