package com.scaffold.webflux.support;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author hui.zhang
 * @date 2022年08月28日 12:20
 */
public class WrapperServerResponse extends ServerHttpResponseDecorator {

    private final ServerWebExchange exchange;

    public WrapperServerResponse(ServerWebExchange exchange) {
        super(exchange.getResponse());
        this.exchange = exchange;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return super.writeWith(DataBufferUtils.join(Flux.from(body)).map(dataBuffer -> {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            DataBufferUtils.release(dataBuffer);
            return content;

        }).flatMap(bytes -> {
            String reqBody = exchange.getAttribute("FLAG_REQUEST");
            String respBody = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("请求reqBody：" + reqBody);
            System.out.println("返回respBody：" + respBody);
            return Mono.just(exchange.getResponse().bufferFactory().wrap(respBody.getBytes()));
        }));
    }
}
