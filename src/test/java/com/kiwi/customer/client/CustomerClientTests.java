package com.kiwi.customer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerClientTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private CustomerClient customerClient;

    private ObjectMapper objectMapper;

    @Autowired
    private ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    @Before
    public void setUp() {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                this.reactiveClientRegistrationRepository, new UnAuthenticatedServerOAuth2AuthorizedClientRepository());
        customerClient = new CustomerClient(WebClient.builder().filter(oauth).build());
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
        Customer customer = customerClient.getCustomerById("1").block();
        Assert.assertEquals("1", customer.getId());
        Assert.assertEquals("John Smith", customer.getName());
        Assert.assertEquals("EXBN", customer.getSegment());
    }

    @Test
    public void testGetCustomersClient() throws Exception {
        givenThat(get(urlEqualTo("/api/mock/customer"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(loadFile("json/customers.json"))));

        customerClient.setCustomerValidationUrl("http://localhost:8089/api/mock/customer");
        List<Customer> customers = customerClient.getCustomers().block();
        Assert.assertEquals("2", customers.get(1).getId());
        Assert.assertEquals("Tailor Swift", customers.get(1).getName());
        Assert.assertEquals("Personal", customers.get(1).getSegment());
    }

    private static String loadFile(String filename) throws Exception {
        InputStream inputStream = CustomerClientTests.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
