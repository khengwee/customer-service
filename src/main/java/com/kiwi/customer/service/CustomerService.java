package com.kiwi.customer.service;

import com.kiwi.customer.client.CustomerClient;
import com.kiwi.customer.web.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerClient customerClient;

    public CustomerService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    public Mono<Customer> getCustomerById(String id) {
        return customerClient.getCustomerById(id);
    }

    public Flux<Customer> getCustomers() {
        return customerClient.getCustomers;
    }
}
