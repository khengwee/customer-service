package com.kiwi.customer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Component
public class KongClient {

    @Autowired
    private WebClient webClient;

    Mono<String> getCustomerCka() {
        return this.webClient
                .get().uri("http://localhost:8090/api/resource/customers")
                //.uri("https://gateway-sit.api.dev.net/RPBWM/interfaces/customer/ISSUER_ZYSC?filter[ckaRequired]=Yes&fields[customer]=suitableStatus")
                .attributes(clientRegistrationId("okta"))
                .retrieve()
                .bodyToMono(String.class);
    }

    Mono<HashMap> getOAuth2AccessToken() {
        final WebClient webClientAuth = webClient.mutate().filter(basicAuthentication("0oaivx2uysUxzlFvd0h7", "jdo1xC5AXlgsR5Wy4zNz82MoO9_kxPZX1gGkVa7d")).build();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "customer_read");
        return webClientAuth
                .post().uri("https://dev-517011.oktapreview.com/oauth2/default/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(requestBody)
                .retrieve()
                .bodyToMono(HashMap.class);
    }
}
