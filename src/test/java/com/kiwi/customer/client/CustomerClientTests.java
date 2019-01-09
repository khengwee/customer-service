package com.kiwi.customer.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.InputStream;

@RunWith(SpringRunner.class)
public class CustomerClientTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    CustomerClient customerClient = new CustomerClient();

    @Test
    public void testGetCustomerClient() throws Exception {
        givenThat(get(urlEqualTo("/interface/api/customer/1"))
                .withHeader("Accept", equalTo("application/vnd.api+json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(loadFile("json/customers.json"))));

        customerClient.getCustomers.subscribe();

        verify(postRequestedFor(urlMatching("/interface/api/customer/[a-z0-9]+"))
                .withRequestBody(matching("*type\": \"customers\"*"))
                .withHeader("Content-Type", notMatching("application/vnd.api+json")));

    }

    private static String loadFile(String filename) throws Exception {
        InputStream inputStream = CustomerClientTests.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream);
    }

}
