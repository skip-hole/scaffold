package com.scaffold.reactive.enhance.webclient;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author hui.zhang
 * @date 2022年09月24日 18:35
 */
public class MetricWebClientCustomizer implements WebClientCustomizer {
    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(new MetricWebClientFilter());
    }
}
