package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.User;
import br.com.webflux.stockquota.service.JsonPlaceHolderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/jsonplaceholder")
@Slf4j
@AllArgsConstructor
public class JsonPlaceHolderWebClientController {

    private final JsonPlaceHolderService service;

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return service.getUsers();
    }

}
