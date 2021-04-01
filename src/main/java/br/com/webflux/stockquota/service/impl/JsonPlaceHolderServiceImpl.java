package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.User;
import br.com.webflux.stockquota.repository.UserRepository;
import br.com.webflux.stockquota.service.JsonPlaceHolderService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = {"users"})
public class JsonPlaceHolderServiceImpl implements JsonPlaceHolderService {
    private static final String USERS_URL_TEMPLATE = "/users";
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Override
    @Cacheable
    public Flux<User> getUsers() {
        return getUsersServices();
    }

    @Override
    @Cacheable
    public Flux<User> generateUsers() {
        return getUsersServices()
                .flatMap(this::getUserById)
                .flatMap(stockFound -> this.save(stockFound.withId(stockFound.getId())));

    }

    private Mono<User> save(User user) {
        return userRepository.save(user);
    }

    private Mono<User> getUserById(User user) {
        return userRepository.findById(user.getId())
                .switchIfEmpty(save(user));
    }

    private Flux<User> getUsersServices() {
        return webClient
                .get()
                .uri(USERS_URL_TEMPLATE)
                .retrieve()
                .bodyToFlux(User.class);
//                .delayElements(Duration.ofSeconds(1));
    }
}
