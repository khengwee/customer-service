package com.kiwi.customer.service;

import com.kiwi.customer.client.Customer;
import com.kiwi.customer.client.CustomerClient;
import com.kiwi.customer.client.CustomerData;
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
        Mono<CustomerData> customerMono = customerClient.getCustomerById(id);

        Mono<CustomerDto> customerDtoMono = customerMono.flatMap(customerData -> {
            CustomerDto customerDto = CustomerMapper.INSTANCE.toCustomerDto(customerData);
            return Mono.just(customerDto);
        });

        return customerDtoMono;
    }

    public Mono<List> getCustomers() {
        Mono<List<CustomerData>> customersMono = customerClient.getCustomers();

        Mono<List> customerDtosMono = customersMono.flatMap(customerDatas -> {
            List customerDtoList = new ArrayList<>();
            for (CustomerData customerData: customerDatas) {
                CustomerDto customerDto = CustomerMapper.INSTANCE.toCustomerDto(customerData);
                customerDtoList.add(customerDto);
            }
            return Mono.just(customerDtoList);
        });
        return customerDtosMono;
    }
}
