package com.kiwi.customer.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KongClientTests {

    @Autowired
    private KongClient kongClient;

    @Test
    public void testGetCustomerCka() throws Exception {
        Mono<String> result = this.kongClient.getCustomerCka();

        StepVerifier.create(result)
                .expectNext("")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetOAuth2AccessToken() throws Exception {
        Mono<HashMap> result = this.kongClient.getOAuth2AccessToken();
        StepVerifier.create(result)
                .expectNext(new HashMap())
                .expectComplete()
                .verify();
    }
}
