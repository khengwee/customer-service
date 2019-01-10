package com.kiwi.customer.config;

import com.kiwi.customer.client.Customer;
import com.kiwi.customer.web.CustomerDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper( CustomerMapper.class );

    @Mappings({
            @Mapping(source = "data.id", target = "id"),
            @Mapping(source = "data.attributes.name", target = "name"),
            @Mapping(source = "data.attributes.segment", target = "segment")
    })
    CustomerDto toCustomerDto(Customer customer);

    @InheritInverseConfiguration
    Customer fromCustomerDto(CustomerDto customerDto);
}