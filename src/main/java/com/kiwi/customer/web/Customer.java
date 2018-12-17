package com.kiwi.customer.web;

public class Customer {

    private String id;
    private String name;
    private String segment;

    public Customer() {}

    public Customer(String id, String name, String segment){
        this.id = id;
        this.name = name;
        this.segment = segment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Override
    public String toString() {
        return String.format("id = %s, name = %s, lastname = %s", id, name, segment);
    }
}