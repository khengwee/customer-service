package com.kiwi.customer.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.kiwi.customer.web.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.valueOf;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
public class CustomerClient {

    private static final MediaType APPLICATION_JSONAPI = valueOf("application/vnd.api+json");

    @Value("${server.backend.customerValidationUrl}")
    private String customerValidationUrl;

    private WebClient webClient;

    public CustomerClient(WebClient webclient) {
        this.webClient = webclient;
    }

    public Mono<Customer> getCustomerById(String id) {
        return getCustomerResponse(id).flatMap(res -> res.bodyToMono(JsonNode.class).flatMap(customerNode -> {
            Customer customer = new Customer();
            customer.setId(customerNode.get("data").get("id").textValue());
            customer.setName(customerNode.get("data").get("attributes").get("name").textValue());
            customer.setSegment(customerNode.get("data").get("attributes").get("segment").textValue());
            return Mono.just(customer);
        }));
    }

    public Mono<List<Customer>> getCustomers() {
        return getCustomersResponse().flatMap(res -> res.bodyToMono(JsonNode.class).flatMap(customersNode -> {
            List<Customer> customers = new ArrayList<>();
            for(JsonNode customerNode: customersNode.get("data")) {
                Customer customer = new Customer();
                customer.setId(customerNode.get("id").textValue());
                customer.setName(customerNode.get("attributes").get("name").textValue());
                customer.setSegment(customerNode.get("attributes").get("segment").textValue());
                customers.add(customer);
            }
            return Mono.just(customers);
        }));
    }

    private Mono<ClientResponse> getCustomerResponse(String id) {
        return this.webClient
                .get().uri(customerValidationUrl + "/{id}", id)
                .accept(APPLICATION_JSONAPI)
                .attributes(clientRegistrationId("okta"))
                .exchange();
    }

    private Mono<ClientResponse> getCustomersResponse() {
        return this.webClient
                .get().uri(customerValidationUrl)
                .accept(APPLICATION_JSONAPI)
                .attributes(clientRegistrationId("okta"))
                .exchange();
    }

    public void setCustomerValidationUrl(String customerValidationUrl) {
        this.customerValidationUrl = customerValidationUrl;
    }

}
