package com.kiwi.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations, ServerOAuth2AuthorizedClientRepository authorizedClients) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);

        // (optional) explicitly opt into using the oauth2Login to provide an access token implicitly
        // oauth.setDefaultOAuth2AuthorizedClient(true);
        // (optional) set a default ClientRegistration.registrationId
        // oauth.setDefaultClientRegistrationId("client-registration-id");
        return WebClient.builder()
                .filter(oauth)
                .build();
    }
}
