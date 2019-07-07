package com.kiwi.customer.mapper;

import com.kiwi.customer.client.CustomerDO;
import com.kiwi.customer.web.CustomerDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributes.name", target = "name")
    @Mapping(source = "attributes.segment", target = "segment")
    CustomerDTO toCustomerDTO(CustomerDO customer);

    @InheritInverseConfiguration
    CustomerDO fromCustomerDTO(CustomerDTO customerDTO);
}