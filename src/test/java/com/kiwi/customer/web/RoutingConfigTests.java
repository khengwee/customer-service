package com.kiwi.customer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwi.customer.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoutingConfigTests {

	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.password}")
	private String password;

	@Autowired
	private WebTestClient webTestClient;

    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper;

    @Before
    public void initSetup() {
        objectMapper = new ObjectMapper();
    }

	@Test
	public void testFindAllCustomers() throws UnsupportedEncodingException {
        // Expected Response
        CustomerDto customer1 = new CustomerDto();
        customer1.setId("01S1374503K");
        customer1.setName("customer1");
        customer1.setSegment("testSegment");
        CustomerDto customer2 = new CustomerDto();
        customer2.setId("01S1374503Z");
        customer2.setName("customer2");
        customer2.setSegment("Priority");

        List<CustomerDto> customerList = new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);

        CustomerDtos customers = new CustomerDtos();
        customers.setCustomerDtos(customerList);

        Mono<CustomerDtos> expectedCustomer = Mono.just(customers);

        // Mock Response
        Mockito.when(this.customerService.getCustomers()).thenReturn(expectedCustomer);

		webTestClient.get().uri("/api/customers")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("{\"customerDtos\":[{\"id\":\"01S1374503K\",\"name\":\"customer1\",\"segment\":\"testSegment\"},{\"id\":\"01S1374503Z\",\"name\":\"customer2\",\"segment\":\"Priority\"}]}");
	}

	@Test
	public void testFindOneCustomers() throws UnsupportedEncodingException {
        // Expected Response
        CustomerDto customer = new CustomerDto();
        customer.setId("1");
        customer.setName("customer1");
        customer.setSegment("testSegment");

        Mono<CustomerDto> expectedCustomer = Mono.just(customer);

        // Mock Response
        Mockito.when(this.customerService.getCustomerById("1")).thenReturn(expectedCustomer);

		webTestClient.get().uri("/api/customers/1")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("{\"id\":\"1\",\"name\":\"customer1\",\"segment\":\"testSegment\"}");
	}
}
