package com.kiwi.customer.service;

import com.kiwi.customer.client.CustomerResourceClient;
import com.kiwi.customer.client.CustomerDO;
import com.kiwi.customer.mapper.CustomerMapper;
import com.kiwi.customer.web.CustomerDTO;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private static final CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

    private final CustomerResourceClient customerResourceClient;

    public CustomerService(CustomerResourceClient customerResourceClient) {
        this.customerResourceClient = customerResourceClient;
    }

    public Mono<CustomerDTO> getCustomerById(String customerId) {
        LOGGER.debug("getCustomerById: customerId={}", customerId);
        return customerResourceClient.getCustomerById(customerId).flatMap(customerDO -> {
            CustomerDTO customerDTO = MAPPER.toCustomerDTO(customerDO);
            LOGGER.debug("getCustomerById: customerDTO={}", customerDTO);
            return Mono.just(customerDTO);
        });
    }

    public Mono<List<CustomerDTO>> getCustomers() {
        LOGGER.debug("getCustomers");
        return customerResourceClient.getCustomers().flatMap(customerDOs -> {
            List<CustomerDTO> customerDTOs = new ArrayList<>();
            for (CustomerDO customerDO: customerDOs) {
                CustomerDTO customerDTO = MAPPER.toCustomerDTO(customerDO);
                customerDTOs.add(customerDTO);
            }
            LOGGER.debug("getCustomers: customerDTOs={}", customerDTOs);
            return Mono.just(customerDTOs);
        });
    }
}
