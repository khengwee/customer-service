package com.kiwi.customer.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwi.customer.web.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.springframework.http.MediaType.valueOf;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
public class CustomerClient {

    private static final MediaType APPLICATION_JSONAPI = valueOf("application/vnd.api+json");

    @Value("${server.backend.customerValidationUrl}")
    private String customerValidationUrl;

    private WebClient webClient;
    private ObjectMapper objectMapper;

    public CustomerClient(WebClient webclient, ObjectMapper objectMapper) {
        this.webClient = webclient;
        this.objectMapper = objectMapper;
    }

    public Mono<CustomerData> getCustomerById(String id) {
        return getCustomerResponse(id).flatMap(res -> res.bodyToMono(JsonNode.class).flatMap(customerNode -> {
            CustomerData customerData = null;
            try {
                customerData = objectMapper.treeToValue(customerNode.get("data"), CustomerData.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Mono.just(customerData);
        }));
    }

    public Mono<List<CustomerData>> getCustomers() {
        return getCustomersResponse().flatMap(res -> res.bodyToMono(JsonNode.class).flatMap(customersNode -> {
            CustomerData[] customerDatas = new CustomerData[customersNode.get("data").size()];
            try {
                customerDatas = objectMapper.treeToValue(customersNode.get("data"), CustomerData[].class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Mono.just(Arrays.asList(customerDatas));
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
