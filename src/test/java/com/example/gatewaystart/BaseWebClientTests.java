package com.example.gatewaystart;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BaseWebClientTests {

    @LocalServerPort
    private int port = 0;

    protected WebTestClient testClient;

    protected String baseUrl;

    @BeforeEach
    public void setup() {
        setup(new ReactorClientHttpConnector(), "http://localhost:" + port);
    }

    protected void setup(ClientHttpConnector httpConnector, String baseUrl) {
        this.baseUrl = baseUrl;
        this.testClient = WebTestClient.bindToServer(httpConnector).baseUrl(this.baseUrl).build();
    }

}
