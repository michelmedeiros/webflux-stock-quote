package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.User;
import br.com.webflux.stockquota.service.JsonPlaceHolderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/jsonplaceholder")
@Slf4j
@AllArgsConstructor
public class JsonPlaceHolderWebClientController {

    private static final String USERS_URL_TEMPLATE = "/users";
    private final WebClient webClient;
    private final JsonPlaceHolderService service;

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/direct/users")
    public Flux<User> getUsersDirect() {
        return webClient
                .get()
                .uri(USERS_URL_TEMPLATE)
                .retrieve()
                .bodyToFlux(User.class);
//                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/generate/users")
    public Flux<User> generateUsers() {
        return service.generateUsers();
    }

}
