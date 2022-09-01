package com.scaffold.webflux.config;

import com.scaffold.webflux.message.MessageAspect;
import com.scaffold.webflux.util.ContextUtils;
import com.scaffold.webflux.util.MessageUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author hui.zhang
 * @date 2022年09月01日 22:23
 */
@Configuration
public class AutoWebFluxPushMessageConfiguration {

    @Bean
    public ContextUtils context() {
        return new ContextUtils();
    }

    @Bean
    @ConditionalOnClass(ServerWebExchange.class)
    public MessageUtils message() {
        return new MessageUtils();
    }

    @Bean
    @ConditionalOnClass(DispatcherHandler.class)
    public MessageAspect messageAspect() {
        return new MessageAspect();
    }
}
