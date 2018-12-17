package com.kiwi.customer.web;

import com.kiwi.customer.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerHandler {

    private final CustomerService customerService;

    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> findOne(ServerRequest request) {
        String customerId = request.pathVariable("id");
        // build notFound response
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        // get customer from repository
        Mono<Customer> customerMono = customerService.getCustomerById(customerId);
        // build response
        return customerMono
                .flatMap(customer -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(customer)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<Customer> customers = customerService.getCustomers();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(customers, Customer.class);
    }

}
