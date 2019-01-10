package com.kiwi.customer.client;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Component
public class KongClient {
    private WebClient webClient = WebClient.create();

    Mono<String> getCustomerCka() {
        return this.webClient
                .get()
                .uri("https://gateway-sit.api.dev.net/RPBWM/interfaces/customer/ISSUER_ZYSC?filter[ckaRequired]=Yes&fields[customer]=suitableStatus")
                .attributes(clientRegistrationId("kong"))
                .exchange()
                .flatMap(response -> response.bodyToMono(String.class));
    }

    Mono<String> getOAuth2AccessToken() {
        // final WebClient webClientAuth = webClient.mutate().filter(basicAuthentication("PeG612yegNfn1fuypmdbcAKMbD4a", "RoUw8eSRxjLHLBf48aXke5Wku70a")).build();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "RPBWMApiService_GET");
        String basicAuth = "PeG612yegNfn1fuypmdbcAKMbD4a:RoUw8eSRxjLHLBf48aXke5Wku70a";
        String encodedBasicAuth = Base64.encodeBase64URLSafeString(basicAuth.getBytes());

        System.out.println("encodedBasicAuth: " + encodedBasicAuth);
        return webClient
                .post().uri("https://identity-sit.api.dev.net/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization: Basic "+encodedBasicAuth)
                .body(fromObject("grant_type=client_credentials&scope=RPBWMApiService_GET"))
                .exchange()
                .flatMap(response -> response.bodyToMono(String.class));
    }
}
