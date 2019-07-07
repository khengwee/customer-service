package com.kiwi.customer.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDO implements Serializable {

    private static final long serialVersionUID = -6265352989415682016L;

    protected String id;
    protected String type;
    protected CustomerAttributeDO attributes;

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

    public CustomerAttributeDO getAttributes() {
        return attributes;
    }

    public void setAttributes(CustomerAttributeDO attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "CustomerDO{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
