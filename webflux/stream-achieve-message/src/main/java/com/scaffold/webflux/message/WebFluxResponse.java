package com.scaffold.webflux.message;

import com.scaffold.webflux.util.ContextUtils;
import com.scaffold.webflux.util.MessageUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author hui.zhang
 * @date 2022年08月28日 12:20
 */
public class WebFluxResponse extends ServerHttpResponseDecorator {

    private final ServerWebExchange exchange;

    public WebFluxResponse(ServerWebExchange exchange) {
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
            String respBody = new String(bytes, StandardCharsets.UTF_8);
            exchange.getAttributes().put(MessageUtils.DEV_OPS_RESPONSE, respBody);
            MessageUtils utils = ContextUtils.getBean(MessageUtils.class);
            Map<String, Object> message = utils.assembleWebFluxMessage(exchange);
            utils.subscribeWebFluxMessage(message);
            return Mono.just(exchange.getResponse().bufferFactory().wrap(respBody.getBytes()));
        }));
    }

}
