package com.scaffold.webmvc.config;

import enhance.resttemplate.MetricInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author hui.zhang
 * @date 2022年09月24日 16:10
 */
@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        OkHttp3ClientHttpRequestFactory httpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        RestTemplate restTemplate = builder.setConnectTimeout(Duration.ofMillis(10000))
                .setConnectTimeout(Duration.ofMillis(10000)).build();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));
        restTemplate.setInterceptors(Collections.singletonList(new MetricInterceptor()));
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        //1.解决中文乱码
        messageConverters.set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
