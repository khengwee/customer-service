package com.kiwi.customer.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

@RunWith(SpringRunner.class)
public class CustomerClientTests {

    private MockWebServer mockWebserver;

    @InjectMocks
    CustomerClient customerClient;

    @Before
    public void setup() throws Exception {
        mockWebserver = new MockWebServer();
        mockWebserver.start();
    }

    @Test
    public void testGetCustomerClient() throws Exception {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(loadFile("json/customers.json"));
        mockWebserver.enqueue(mockResponse);

        customerClient.getCustomers.subscribe();

        RecordedRequest request = mockWebserver.takeRequest();
        Assert.assertEquals("http://localhost:8092", request.getPath());

    }

    @After
    public void tearDown() throws Exception {
        mockWebserver.shutdown();
    }

    private static String loadFile(String filename) throws Exception {
        InputStream inputStream = CustomerClientTests.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream);
    }

}
