package com.kiwi.customer.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Attr;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@RunWith(SpringRunner.class)
public class CustomerClientTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
//
//    @MockBean
//    private CustomerClient customerClient;

    @Test
    public void testGetCustomerClient() throws Exception {
        givenThat(get(urlEqualTo("/api/mock/customer"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(loadFile("json/customers.json"))));

//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet request = new HttpGet("http://localhost:8089/api/mock/customer");
//        HttpResponse httpResponse = httpClient.execute(request);
//        String stringResponse = convertHttpResponseToString(httpResponse);

        CustomerClient customerClient = new CustomerClient();
        Mono<Customers> customersMono = customerClient.getCustomers();

        Customers customers = new Customers();
        Data data1 = new Data();
        data1.setId("1");
        data1.setType("customers");
        Attributes attributes = new Attributes();
        attributes.setName("John Smith");
        attributes.setSegment("Priority");
        data1.setAttributes(attributes);
        Links links = new Links();
        links.setSelf("http://localhost:8081/api/customers/1");
        data1.setLinks(links);

        Link<Data> datas = new ArrayList<>();

        StepVerifier.create(customersMono)
        .expectNext()
        .verifyComplete();
//        verify(getRequestedFor(urlEqualTo("/api/mock/customer")));
        //assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        //assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        //assertThat(stringResponse, containsString("\"type\": \"customers\""));


    }

    private static String loadFile(String filename) throws Exception {
        InputStream inputStream = CustomerClientTests.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream);
    }

    private String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }

}
