package com.scaffold.webflux;

import com.scaffold.webflux.model.Order;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author hui.zhang
 * @date 2022年08月27日 16:49
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class WebfluxApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void webClientTest(){
        WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body("{ \"key\" : \"value\"}")
                                .build())
                ).build();
    }

    @Test
    public void shouldBeAbleToPostOrder() {
        Order order = new Order("123456");
        webTestClient
                .post()
                .uri("/order/create?userId=123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(order))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldBeNotFound() {
        webTestClient
                .get()
                .uri("/order/list?userId=123")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldBeAbleToFindOrder() {
        shouldBeAbleToPostOrder();
        webTestClient
                .get()
                .uri("/order/detail/123456?userId=123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .consumeWith(e -> MatcherAssert.assertThat(e.getResponseBody(), Matchers.hasProperty("id", Matchers.equalTo("123456"))));
    }

    @Test
    public void shouldBeAbleToDeleteOrder() {
        shouldBeAbleToPostOrder();
        webTestClient
                .post()
                .uri("/order/delete?userId=123&name=lily")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("id", "123456"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .consumeWith(e -> MatcherAssert.assertThat(e.getResponseBody(), Matchers.hasProperty("id", Matchers.equalTo("123456"))));
    }
}
