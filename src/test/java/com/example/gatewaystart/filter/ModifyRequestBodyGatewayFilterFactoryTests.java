package com.example.gatewaystart.filter;

import com.example.gatewaystart.BaseWebClientTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ModifyRequestBodyGatewayFilterFactoryTests extends BaseWebClientTests {

    @Test
    public void modifyRequestBodyFilterWorks() {
        String body = "{\"name\":\"test\", \"content\":\"test\"}";

        testClient.method(HttpMethod.GET)
                .uri("/headers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), String.class)
                .exchange()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    System.out.println(result);
                    System.out.println(new String(result.getRequestBodyContent()));
                    System.out.println(result.getResponseBody());
                });
    }

    @EnableAutoConfiguration
    @SpringBootConfiguration
    public static class TestConfig {

        @Value("${test.uri}")
        String uri;

        @Bean
        public RouteLocator routeLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("rewrite_request_body",
                        r -> r.path("/headers")
                        .filters(f -> f.modifyRequestBody(Hello.class, Hello.class, MediaType.APPLICATION_JSON_VALUE,
                                (exchange, hello) -> Mono.just(new Hello(hello.getName().toUpperCase(), "world2"))))
                        .uri(uri)
                    )
                    .build();
        }
    }


    static class Hello {
        String name;
        String content;

        public Hello() {

        }

        public Hello(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
