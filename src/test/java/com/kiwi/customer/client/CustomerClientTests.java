package com.kiwi.customer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerClientTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private CustomerClient customerClient;

    private ObjectMapper objectMapper;

    @Before
    public void initSetup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetOneCustomerClient() throws Exception {
        givenThat(get(urlEqualTo("/api/mock/customer/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(loadFile("json/customers_1.json"))));

        customerClient.setCustomerValidationUrl("http://localhost:8089/api/mock/customer");
        Mono<Customer> customerMono = customerClient.getCustomerById("1");
        Mono<String> customerStrMono = customerMono.flatMap(customer -> {
            String customerString = null;
            try {
                customerString = objectMapper.writeValueAsString(customer);
            } catch (Exception e) {
            }
            return Mono.just(customerString);
        });

        String customerString = loadFile("json/customers_1.json");
        customerString = customerString.replaceAll("\\s","");
        System.out.println("customersString " + customerString);
        StepVerifier.create(customerStrMono)
                .expectNext(customerString)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetCustomersClient() throws Exception {
        givenThat(get(urlEqualTo("/api/mock/customer"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(loadFile("json/customers.json"))));

        customerClient.setCustomerValidationUrl("http://localhost:8089/api/mock/customer");
        Mono<Customers> customersMono = customerClient.getCustomers();
        Mono<String> customersStrMono = customersMono.flatMap(customers -> {
            String customersString = null;
            try {
                customersString = objectMapper.writeValueAsString(customers);
            } catch (Exception e) {
            }
            return Mono.just(customersString);
        });

        String customersString = loadFile("json/customers.json");
        customersString = customersString.replaceAll("\\s","");
        System.out.println("customersString " + customersString);
        StepVerifier.create(customersStrMono)
                .expectNext(customersString)
                .expectComplete()
                .verify();
    }

    private static String loadFile(String filename) throws Exception {
        InputStream inputStream = CustomerClientTests.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
