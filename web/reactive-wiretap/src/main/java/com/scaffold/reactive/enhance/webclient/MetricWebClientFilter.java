package com.scaffold.reactive.enhance.webclient;

import com.scaffold.reactive.constant.WebFluxConstant;
import com.scaffold.reactive.trace.Tracer;
import com.scaffold.reactive.util.ContextUtils;
import com.scaffold.reactive.util.JacksonUtils;
import com.scaffold.reactive.wiretap.webclient.WebClientFinishWiretap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scaffold.reactive.constant.WebFluxConstant.HEADER_TRACE_ID;


@Slf4j
public class MetricWebClientFilter implements ExchangeFilterFunction {

    private static final String PARAM_SEPARATE = "&";
    private static final String KV_SEPARATE = "=";

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        String traceId = Tracer.getTraceId();
        boolean isRepeatFlow = Tracer.isRepeatFlow();
        return next.exchange(ClientRequest.from(request)
                .header(HEADER_TRACE_ID, traceId)
                .header(WebFluxConstant.REPEAT_FLOW_BOOLEAN, String.valueOf(isRepeatFlow))
                .build())
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)
                        .map(resp -> {
                            sendRecord(traceId, isRepeatFlow, resp, request, clientResponse);
                            return ClientResponse.create(clientResponse.statusCode())
                                    .body(resp)
                                    .headers(httpHeaders -> httpHeaders.addAll(clientResponse.headers().asHttpHeaders()))
                                    .cookies(cookie -> cookie.addAll(clientResponse.cookies()))
                                    .statusCode(clientResponse.statusCode())
                                    .build();
                        }));
    }

    private void sendRecord(String traceId, boolean isRepeatFlow, String resp, ClientRequest request, ClientResponse clientResponse) {
        if (isRepeatFlow) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("requestHeaders", getHeadersMap(request.headers()));
        params.put("requestMethod", request.method().name());
        params.put("requestURI", request.url().getPath());
        params.put("requestURL", request.url().toString());
        params.put("requestParams", getParamMap(request.url().getQuery()));
        params.put("requestBody", parserBody(request.body()));
        params.put(HEADER_TRACE_ID, traceId);
        Map<String, Object> result = new HashMap<>();
        result.put("responseBody", resp);
        result.put("responseStatusCode", clientResponse.statusCode().name());
        result.put("responseHeaders", clientResponse.headers().asHttpHeaders());
        ContextUtils.getBean(WebClientFinishWiretap.class).finish(params, result);
    }

    private String parserBody(BodyInserter<?, ? super ClientHttpRequest> bodyInserter) {
        Class<? extends BodyInserter> clazz = bodyInserter.getClass();
        Field data;
        try {
            data = clazz.getDeclaredFields()[0];
            data.setAccessible(true);
            Object result = data.get(bodyInserter);
            data.setAccessible(false);
            if (result instanceof Mono) {
                result = ((Mono) result).block();
            }
            return JacksonUtils.toJson(result);
        } catch (Exception e) {
            log.error("webclient request body is error.", e);
        }
        return null;
    }

    private Map<String, String> getParamMap(String paramStr) {

        Map<String, String> paramMap = new HashMap<String, String>();
        if (StringUtils.isBlank(paramStr) || "null".equalsIgnoreCase(paramStr)) {
            return paramMap;
        }

        String[] split = paramStr.split(PARAM_SEPARATE);
        if (split.length == 0) {
            return paramMap;
        }

        for (String s : split) {
            String[] param = s.split(KV_SEPARATE);
            if (param.length == 0) {
                continue;
            }
            paramMap.put(param[0], param[1]);
        }

        return paramMap;
    }

    private Map<String, List<String>> getHeadersMap(HttpHeaders headers) {
        Map<String, List<String>> listMap = new HashMap<>();
        if (headers == null) {
            return listMap;
        }
        for (String name : headers.keySet()) {
            listMap.put(name, headers.get(name));
        }
        return listMap;
    }
}
