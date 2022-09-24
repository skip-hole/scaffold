package wiretap.webflux;


import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import trace.Tracer;
import util.ContextUtils;
import util.JacksonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static constant.WebFluxConstant.*;


/**
 * WebFlux 请求响应获取埋点
 *
 * @author hui.zhang
 * @date 2022年09月07日 10:02
 */
public class WebFluxFinishWiretap {


    public synchronized Map<String, Object> assembleRequest(ServerWebExchange exchange) {
        Map<String, Object> result = new HashMap<>();
        result.put("requestURI", exchange.getRequest().getURI().getPath());
        result.put("requestURL", exchange.getRequest().getURI().toString());
        result.put("method", exchange.getRequest().getMethodValue());
        result.put("port", exchange.getRequest().getURI().getPort());
        result.put("contentType", getContentType(exchange.getRequest()));
        result.put("headers", exchange.getRequest().getHeaders().toSingleValueMap());
        result.put("paramsMap", getParamsMap(exchange.getRequest().getQueryParams()));
        result.put("body", exchange.getAttribute(DEV_OPS_REQUEST));
        result.put(HEADER_TRACE_ID, exchange.getAttribute(HEADER_TRACE_ID));
        return result;
    }

    private Map<String, String[]> getParamsMap(MultiValueMap<String, String> multiValueMap) {
        Map<String, String[]> paramsMap = new HashMap<>();
        if (multiValueMap == null || multiValueMap.isEmpty()) {
            return paramsMap;
        }
        for (String key : multiValueMap.keySet()) {
            paramsMap.put(key, Arrays.stream(multiValueMap.get(key).toArray()).toArray(String[]::new));
        }
        return paramsMap;

    }

    public Object finish(Map<String, Object> request, String responseBody) {
        return responseBody;

    }

    public synchronized void initContext(ServerWebExchange exchange) {
        String repeatTraceId = getTraceId(exchange.getRequest(), HEADER_TRACE_ID);
        Tracer.start(repeatTraceId);
        exchange.getAttributes().put(HEADER_TRACE_ID, Tracer.getTraceId());
        exchange.getAttributes().put(REPEAT_FLOW_BOOLEAN, Tracer.isRepeatFlow());
    }

    private void clearContext(ServerWebExchange exchange) {
        exchange.getAttributes().remove(DEV_OPS_REQUEST);
        exchange.getAttributes().remove(HEADER_TRACE_ID);
        exchange.getAttributes().remove(REPEAT_FLOW_BOOLEAN);
        Tracer.end();
    }

    public synchronized Object error(ServerWebExchange exchange, Object ret) {

        Map<String, Object> request = assembleRequest(exchange);
        if (ret instanceof Mono) {
            ret = ((Mono) ret).onErrorResume(err -> {
                sendRecord(request,err);
                return err instanceof Throwable ? Mono.error((Throwable) err) : Mono.just(err);
            }).doFinally(cs->clearContext(exchange));
        } else if (ret instanceof Flux) {
            ret = ((Flux) ret).onErrorResume(err -> {
                sendRecord(request,err);
                return err instanceof Throwable ? Flux.error((Throwable) err) : Flux.just(err);
            }).doFinally(cs->clearContext(exchange));
        }
        return ret;
    }

    private void sendRecord(Map<String,Object> request,Object err){
        if (Tracer.isRepeatFlow()) {
            return;
        }
        ContextUtils.getBean(WebFluxFinishWiretap.class).finish(request, JacksonUtils.toJson(err));
    }


    private static String getContentType(ServerHttpRequest request) {
        MediaType mediaType = request.getHeaders().getContentType();
        if (mediaType == null) {
            return "";
        }
        return mediaType.toString();
    }

}
