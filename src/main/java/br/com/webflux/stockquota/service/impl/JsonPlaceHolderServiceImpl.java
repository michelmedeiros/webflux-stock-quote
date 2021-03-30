package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.User;
import br.com.webflux.stockquota.repository.UserRepository;
import br.com.webflux.stockquota.service.JsonPlaceHolderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class JsonPlaceHolderServiceImpl implements JsonPlaceHolderService {
    private static final String USERS_URL_TEMPLATE = "/users";
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Override
    public Flux<User> getUsers() {
        Flux<User> users = getUsersServices();
        return users
                .flatMap(this::getUserById)
                .flatMap(stockFound -> this.save(stockFound.withId(stockFound.getId())));

        /*
        return ordersDbRepo.findById("order-id")
        .switchIfEmpty(
                missingOrdersDb.save(Order("order-id", false))
        )
         */
    }

    private Flux<User> saveAll(Flux<User> users) {
        return userRepository.saveAll(users);
    }

    private Mono<User> save(User user) {
        return userRepository.save(user);
    }

    private Mono<User> getUserById(User user) {
        return userRepository.findById(user.getId())
                .switchIfEmpty(save(user));
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(Integer id) {
        var message = String.format("User by id not found %s", id);
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
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
