package com.example.gatewaystart.filter;

import com.example.gatewaystart.BaseWebClientTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.MapRequestHeaderGatewayFilterFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(profiles = "map-request-header-filter")
public class MapRequestHeaderGatewayFilterFactoryTests extends BaseWebClientTests {

    @Test
    public void mapRequestHeaderFilterWorks() {
        testClient
                .get()
                .uri("/headers")
                .header("X-Request-Blue", "XRB")
                .exchange()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    Map<String, Map<String, String>> map = result.getResponseBody();
                    Map<String, String> requestHeader = Optional.ofNullable(map.get("headers")).orElse(new HashMap<>());

                    assertThat(requestHeader.containsKey("X-Request-Red")).isTrue();
                    assertThat(requestHeader.get("X-Request-Red")).isEqualTo("XRB");
                });
    }

    /*
    @EnableAutoConfiguration
    @SpringBootConfiguration
    public static class TestConfig {

        @Value("${test.uri}")
        String uri;

        @Bean
        public RouteLocator routeLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("add_request_header",
                        r -> r.path("/headers")
                        .filters(f -> f.addRequestHeader("X-Request-Example", "ABC"))
                        .uri(uri)
                    )
                    .build();
        }
    }
     */


}
