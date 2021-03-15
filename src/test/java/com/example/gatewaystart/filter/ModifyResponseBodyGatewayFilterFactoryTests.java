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
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ModifyResponseBodyGatewayFilterFactoryTests extends BaseWebClientTests {

    @Test
    public void modifyResponseBodyFilterWorks() {
        testClient.method(HttpMethod.GET)
                .uri("/headers")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    System.out.println(result);
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
                    .route("rewrite_reponse_body",
                        r -> r.path("/headers")
                        .filters(f -> f.modifyResponseBody(Map.class, Map.class,
                                (exchange, s) -> {
                                    s.put("X-Response-Example", "ABC");
                                    return Mono.just(s);
                                }))
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
