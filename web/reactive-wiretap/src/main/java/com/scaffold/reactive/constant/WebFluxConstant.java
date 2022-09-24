package com.scaffold.reactive.constant;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

public class WebFluxConstant {


    public static final String DEV_OPS_REQUEST = "DEV_OPS_REQUEST";
    public static final String DEV_OPS_RESPONSE = "DEV_OPS_RESPONSE";

    /**
     * 透传给下游的traceId；需要利用traceId串联回放流程
     */
    public static final String HEADER_TRACE_ID = "Repeat-TraceId";

    /**
     * 透传给下游的traceId；跟{@code HEADER_TRACE_ID}的差异在于，{@code HEADER_TRACE_ID_X}表示一次回放请求；需要进行Mock
     */
    public static final String HEADER_TRACE_ID_X = "Repeat-TraceId-X";

    public static final String REPEAT_FLOW_BOOLEAN = "Repeat-Flow-Boolean";


    public static String getTraceId(ServerHttpRequest request, String traceIdKey) {
        String traceId = request.getHeaders().getFirst(traceIdKey);
        if (!StringUtils.hasText(traceId)) {
            traceId = request.getQueryParams().getFirst(traceIdKey);
        }
        return traceId;
    }

}
