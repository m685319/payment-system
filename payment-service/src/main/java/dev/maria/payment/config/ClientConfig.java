package dev.maria.payment.config;

import dev.maria.payment.client.OrderClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Bean
    public OrderClient orderClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();

        return factory.createClient(OrderClient.class);
    }
}
