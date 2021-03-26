package br.com.webflux.stockquota.controller;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.exception.CustomAttributes;
import br.com.webflux.stockquota.integration.StatusInvestClient;
import br.com.webflux.stockquota.repository.StockQuoteReactiveRepository;
import br.com.webflux.stockquota.service.StockQuoteReactiveService;
import br.com.webflux.stockquota.service.impl.StockQuoteReactiveServiceImpl;
import br.com.webflux.stockquota.service.impl.YahooFinancialQuoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(StockReactiveController.class)
@Import({StatusInvestClient.class, StockQuoteReactiveServiceImpl.class, YahooFinancialQuoteServiceImpl.class, CustomAttributes.class})
public class StockQuotaReactiveControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private StatusInvestClient statusInvestClient;

    @MockBean
    private StockQuoteReactiveService stockQuoteReactiveServiceMock;

    @MockBean
    private StockQuoteReactiveRepository stockQuoteReactiveRepositoryMock;

    private final Stock stock = createValidStock();

    @BeforeEach
    void setUp() {
        BDDMockito.when(stockQuoteReactiveServiceMock.getStockByTicketName(any())).thenReturn(Mono.just(stock));
        BDDMockito.when(stockQuoteReactiveRepositoryMock.findFirstByTicket(any())).thenReturn(Mono.just(stock));
    }

    @Test
    @DisplayName("Search by ticket - Returns a mono of stock when it exists")
    void searchByTicketReturnMonoStockWhenSuccess() {
        testClient.get()
                .uri("/stocks/reactive/search/mono/{ticket}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(stock.getName())
                .jsonPath("$.ticket").isEqualTo(stock.getTicket());
    }

    private Stock createValidStock() {
        return Stock.builder()
                .ticket("LREN3")
                .name("Lojas Renner")
                .build();
    }
}
