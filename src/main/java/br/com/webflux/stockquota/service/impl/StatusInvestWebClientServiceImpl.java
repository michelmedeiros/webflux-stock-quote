package br.com.webflux.stockquota.service.impl;

import br.com.webflux.stockquota.domain.Stock;
import br.com.webflux.stockquota.integration.dto.StockDTO;
import br.com.webflux.stockquota.integration.webclient.StatusInvestWebClient;
import br.com.webflux.stockquota.service.StatusInvestWebClientService;
import br.com.webflux.stockquota.service.StockQuoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@AllArgsConstructor
public class StatusInvestWebClientServiceImpl implements StatusInvestWebClientService {

    private final StatusInvestWebClient webClient;
    private final StockQuoteService stockQuoteService;


    @Cacheable("stocks")
    private Flux<StockDTO> getStocksByTicket(String ticket) {
        return webClient.getStocks(ticket);
    }

    @Override
    public Flux<Stock> getStocks(String ticker) {
        try {
            log.info("Start find stocks by ticker {}", ticker);
            Flux<StockDTO> stocks = this.getStocksByTicket(ticker);
            log.info("Finish find stocks by ticker {}", ticker);

            return stocks.flatMap(stock -> stockQuoteService.getStockByTicketName(stock.getCode()))
                  .flatMap(stockFound -> stockQuoteService.save(stockFound.withId(stockFound.getId())))
                  .switchIfEmpty(stockQuoteService.saveAll(stocks));
        } catch (Exception ex) {
            log.error("Error to call Status Invest", ex);
        } finally {
            log.info("Finish processing stocks by ticker {}", ticker);
        }
        return null;
    }

}
