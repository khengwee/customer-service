package com.kiwi.customer.client;

import com.kiwi.customer.web.CustomerDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.valueOf;

@Component
public class CustomerClient {

    private Map<String, CustomerDto> customers = new HashMap<>();
    public static final MediaType APPLICATION_JSONAPI = valueOf("application/vnd.api+json");

    private WebClient client = WebClient.create("http://localhost:8593");

    public Mono<Customer> getCustomerById(String id) {
        return getCustomerResponse(id).flatMap(res -> res.bodyToMono(Customer.class));
    }

    public Mono<Customers> getCustomers() {
        return getCustomersResponse.flatMap(res -> res.bodyToMono(Customers.class));
    }

    private Mono<ClientResponse> getCustomerResponse(String id) {
        return this.client.get()
                .uri("/api/mock/customer/" + id)
                .exchange();
    }

    private Mono<ClientResponse> getCustomersResponse = this.client.get()
            .uri("/api/mock/customer")
            .exchange();

}
