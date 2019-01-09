package com.kiwi.customer.client;

import com.kiwi.customer.web.Customer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.valueOf;

@Component
public class CustomerClient {

    private Map<String, Customer> customers = new HashMap<>();
    public static final MediaType APPLICATION_JSONAPI = valueOf("application/vnd.api+json");

    private WebClient client = WebClient.create("http://localhost:8089");
    // http://localhost:9090/interface/api/customer/1?filter[ckaRequired]=true&fields[customer]=ckasStatus

    public Mono<Customer> getCustomerById(String id) {
        return Mono.just(customers.get(id));
    }

    public Flux<Customer> getCustomers = client.get()
            .uri("/interface/api/customer/1")
            .accept(APPLICATION_JSONAPI)
            .exchange()
            .flatMapMany(res -> res.bodyToFlux(Customer.class));

//    private WebClient client = WebClient.create("http://localhost:8092");
//
//    private Mono<ClientResponse> result = client.get()
//            .uri("/hello")
//            .accept(MediaType.TEXT_PLAIN)
//            .exchange();
//
//    public String getResult() {
//        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
//    }
}
