package com.scaffold.webflux.util;

import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hui.zhang
 * @date 2022年09月01日 20:21
 */
public class MessageUtils {


    public static final String DEV_OPS_REQUEST = "DEV_OPS_REQUEST";
    public static final String DEV_OPS_RESPONSE = "DEV_OPS_RESPONSE";

    public Map<String, Object> assembleMessage(ServerWebExchange exchange) {
        Map<String, Object> result = new HashMap<>();
        result.put("requestURI", exchange.getRequest().getURI().getPath());
        result.put("requestURL", exchange.getRequest().getURI().toString());
        result.put("method", exchange.getRequest().getMethodValue());
        result.put("port", exchange.getRequest().getURI().getPort());
        result.put("contentType", exchange.getRequest().getHeaders().getContentType().toString());
        result.put("headers", exchange.getRequest().getHeaders().toSingleValueMap());
        result.put("paramsMap", exchange.getRequest().getQueryParams());
        result.put("body", exchange.getAttribute(DEV_OPS_REQUEST));
        result.put("response", exchange.getAttribute(DEV_OPS_RESPONSE));
        return result;

    }

    public void pushMessage(Map<String, Object> message) {
        //推送捕捉webflux 请求响应数据
    }
}
