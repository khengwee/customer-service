package com.kiwi.customer.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerData implements Serializable {

    protected String id;
    protected String type;
    protected Customer attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer getAttributes() {
        return attributes;
    }

    public void setAttributes(Customer attributes) {
        this.attributes = attributes;
    }
}
