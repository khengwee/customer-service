package com.kiwi.customer.config;

import com.kiwi.customer.web.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RoutingConfig {

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler) {
        return RouterFunctions.route()
                .GET("/api/customers/{id}", accept(MediaType.APPLICATION_JSON), customerHandler::findOne)
                .GET("/api/customers", accept(MediaType.APPLICATION_JSON), customerHandler::findAll)
                .build();
    }
}
