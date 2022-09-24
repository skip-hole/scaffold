package com.scaffold.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author hui.zhang
 * @date 2022年09月24日 19:13
 */
@Configuration
public class WebClientConfig {


    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
