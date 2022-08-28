package com.scaffold.webflux.support;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

/**
 * @author hui.zhang
 * @date 2022年08月28日 10:02
 */
public class WrapperServerRequest extends ServerHttpRequestDecorator {

    private final ServerWebExchange exchange;

    public WrapperServerRequest(ServerWebExchange exchange) {
        super(exchange.getRequest());
        this.exchange = exchange;
    }


    @Override
    public Flux<DataBuffer> getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return super.getBody().doOnNext(dataBuffer -> {
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String bodyStr = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                exchange.getAttributes().put("requestBody", bodyStr);
                System.out.println("打印body：" + bodyStr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
