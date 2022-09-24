package com.scaffold.webflux.support;

import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;


public class ModifiedServerWebExchange extends ServerWebExchangeDecorator {

    public ModifiedServerWebExchange(ServerWebExchange delegate) {
        super(delegate);
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return super.getFormData()
                .map(Crypto::decrypt);
    }
}
