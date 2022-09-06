package com.scaffold.webflux.message;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author hui.zhang
 * @date 2022年09月03日 20:17
 */
public class WebClientRequest extends ClientHttpRequestDecorator {
    public WebClientRequest(ClientHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public DataBufferFactory bufferFactory() {
        return super.bufferFactory();
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return super.writeWith(DataBufferUtils.join(Flux.from(body)).map(dataBuffer -> {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            DataBufferUtils.release(dataBuffer);
            return content;

        }).flatMap(bytes -> {
            String respBody = new String(bytes, StandardCharsets.UTF_8);
            return Mono.just(this.bufferFactory().wrap(respBody.getBytes()));
        }));
    }
}
