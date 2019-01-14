package com.kiwi.customer.service;

import com.kiwi.customer.client.Customer;
import com.kiwi.customer.client.CustomerClient;
import com.kiwi.customer.config.CustomerMapper;
import com.kiwi.customer.web.CustomerDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerClient customerClient;

    public CustomerService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    public Mono<CustomerDto> getCustomerById(String id) {
        Mono<Customer> customerMono = customerClient.getCustomerById(id);

        Mono<CustomerDto> customerDtoMono = customerMono.flatMap(customer -> {
            CustomerDto customerDto = CustomerMapper.INSTANCE.toCustomerDto(customer);
            return Mono.just(customerDto);
        });

        return customerDtoMono;
    }

    public Mono<List> getCustomers() {
        Mono<List<Customer>> customersMono = customerClient.getCustomers();

        Mono<List> customerDtosMono = customersMono.flatMap(customers -> {
            //CustomerDtos customerDtos = new CustomerDtos();
            List customerDtoList = new ArrayList<>();
            for (Customer customer: customers) {
                CustomerDto customerDto = CustomerMapper.INSTANCE.toCustomerDto(customer);
                customerDtoList.add(customerDto);
            }
            //customerDtos.setCustomerDtos(customerDtoList);
            return Mono.just(customerDtoList);
        });
        return customerDtosMono;
    }
}
