package com.example.gatewaystart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayStartApplication {

    @Autowired
    private GatewayFilter customFilter;

    public static void main(String[] args) {
        SpringApplication.run(GatewayStartApplication.class, args);
    }

    /*
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")  // predicate
                        .filters(f -> f.filter(customFilter).addRequestHeader("Hello", "World")) // filter
                        .uri("http://httpbin.org:80"))
                .build();
    }
     */




}
