package com.kiwi.customer.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoutingConfigTests {

	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.password}")
	private String password;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void testFindAllCustomers() throws UnsupportedEncodingException {
		webTestClient.get().uri("/api/customers")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("[{\"id\":\"1\",\"name\":\"John Smith\",\"segment\":\"Priority\"},{\"id\":\"2\",\"name\":\"Tailor Swift\",\"segment\":\"Personal\"}]");
	}

	@Test
	public void testFindOneCustomers() throws UnsupportedEncodingException {
		webTestClient.get().uri("/api/customers/1")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("{\"id\":\"1\",\"name\":\"John Smith\",\"segment\":\"Priority\"}");
	}
}
