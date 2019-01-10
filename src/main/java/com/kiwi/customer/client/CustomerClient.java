package com.kiwi.customer.client;

import com.kiwi.customer.web.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
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

    private static final MediaType APPLICATION_JSONAPI = valueOf("application/vnd.api+json");
    private WebClient webClient = WebClient.create();

    @Value("${server.backend.customerValidationUrl}")
    private String customerValidationUrl;

    public Mono<Customer> getCustomerById(String id) {
        return getCustomerResponse(id).flatMap(res -> res.bodyToMono(Customer.class));
    }

    public Mono<Customers> getCustomers() {
        return getCustomersResponse().flatMap(res -> res.bodyToMono(Customers.class));
    }

    private Mono<ClientResponse> getCustomerResponse(String id) {
        return this.webClient.get()
                .uri(customerValidationUrl + "/{id}", id)
                .exchange();
    }

    private Mono<ClientResponse> getCustomersResponse() {
        return this.webClient.get()
                .uri(customerValidationUrl)
                .exchange();
    }

    public void setCustomerValidationUrl(String customerValidationUrl) {
        this.customerValidationUrl = customerValidationUrl;
    }

}
