package com.example.demo.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gargg on 25/10/17.
 */
@Configuration
public class GameConfig {

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("userId", "Hy-ekYipW");
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
