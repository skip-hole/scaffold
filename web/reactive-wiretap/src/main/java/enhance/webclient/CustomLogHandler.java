package enhance.webclient;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import trace.Tracer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constant.WebFluxConstant.HEADER_TRACE_ID;
import static java.nio.charset.Charset.defaultCharset;

/**
 * WebClient 请求响应获取埋点挂载类
 *
 * @author hui.zhang
 * @date 2022年09月07日 10:02
 */
@Slf4j
public class CustomLogHandler extends ChannelDuplexHandler {

    private static final String QUESTION_SEPARATE = "\\?";
    private static final String PARAM_SEPARATE = "&";
    private static final String KV_SEPARATE = "=";

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Map<String, Object> params = new HashMap<>();
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            params.put("requestHeaders", getHeadersMap(request.headers()));
            params.put("requestMethod", request.method().name());
            params.put("requestURI", request.uri());
            params.put("requestParams", getRequestParams(request.uri()));
            params.put("requestBody", request.content().toString(defaultCharset()));
            params.put(HEADER_TRACE_ID, Tracer.getTraceId());
            request.headers().add(HEADER_TRACE_ID, Tracer.getTraceId());
        } else if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            params.put("responseHeaders", getHeadersMap(request.headers()));
            params.put("requestMethod", request.method().name());
            params.put("requestURI", request.uri());
            params.put("requestParams", getRequestParams(request.uri()));
            params.put(HEADER_TRACE_ID, Tracer.getTraceId());
            request.headers().add(HEADER_TRACE_ID, Tracer.getTraceId());
        } else if (msg instanceof FullHttpMessage) {
            FullHttpMessage message = (FullHttpMessage) msg;
            params.put("requestBody", message.content().toString(defaultCharset()));
        }
        super.write(ctx, msg, promise);
    }

    private Map<String, String> getRequestParams(String url) {
        Map<String, String> paramMap = new HashMap<>();
        String[] urlArr = url.split(QUESTION_SEPARATE);
        if (urlArr.length < 2) {
            return paramMap;
        }
        String paramStr = urlArr[1];
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
        for (String name : headers.names()) {
            listMap.put(name, headers.getAll(name));
        }
        return listMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            String body = response.content().toString(defaultCharset());
            result.put("responseBody",body);
            //ContextUtils.getBean(WebClientFinishWiretap.class).finish(result, result);
        } else if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
        } else if (msg instanceof DefaultLastHttpContent) {
            DefaultLastHttpContent httpContent = (DefaultLastHttpContent) msg;
            String body = httpContent.content().toString(defaultCharset());
            result.put("responseBody",body);
            //ContextUtils.getBean(WebClientFinishWiretap.class).finish(result, result);
        }
        super.channelRead(ctx, msg);
    }
}
