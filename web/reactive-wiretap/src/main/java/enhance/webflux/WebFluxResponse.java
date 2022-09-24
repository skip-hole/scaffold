package enhance.webflux;



import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import util.ContextUtils;
import wiretap.webflux.WebFluxFinishWiretap;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static constant.WebFluxConstant.REPEAT_FLOW_BOOLEAN;


/**
 * WebFlux 响应装饰增强类，获取响应body
 *
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
            boolean repeatFLow = (boolean) exchange.getAttributes().getOrDefault(REPEAT_FLOW_BOOLEAN, false);
            if (!repeatFLow) {
                sendRecord(respBody);
            }
            return Mono.just(exchange.getResponse().bufferFactory().wrap(respBody.getBytes()));
        }));
    }

    private void sendRecord(String respBody) {
        WebFluxFinishWiretap handler = ContextUtils.getBean(WebFluxFinishWiretap.class);
        Map<String, Object> result = handler.assembleRequest(exchange);
        handler.finish(result, respBody);
    }

}
