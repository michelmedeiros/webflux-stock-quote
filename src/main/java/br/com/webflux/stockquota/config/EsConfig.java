package br.com.webflux.stockquota.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
@EnableReactiveElasticsearchRepositories
public class EsConfig extends AbstractReactiveElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.client.reactive.endpoints}")
    private String elassandraHostAndPort;

    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elassandraHostAndPort)
                .withWebClientConfigurer(webClient -> {
                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(-1))
                            .build();
                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
                })
                .build();

        return ReactiveRestClients.create(clientConfiguration);
    }

    @Bean
    public ReactiveElasticsearchOperations elasticsearchTemplate() {
        return new ReactiveElasticsearchTemplate(reactiveElasticsearchClient());
    }
}