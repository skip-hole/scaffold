package com.scaffold.reactive.enhance.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class MetricInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ClientHttpResponse response = execution.execute(request, body);
        stopWatch.stop();
        log.info("【外部接口调用】：{},响应时间：{}ms,请求参数:{},返回值:{}",
                request.getURI(),
                stopWatch.getTotalTimeMillis(),
                body != null ? new String(body, "UTF-8") : null, StreamUtils.copyToString(response.getBody(), Charset.defaultCharset())
        );
        return response;
    }

//    /**
//     * ClientHttpRequestFactory is not BufferingClientHttpRequestFactory,then InputStream is only read one.
//     * So can use copy method.
//     */
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        ClientHttpResponse response = execution.execute(request, body);
//        stopWatch.stop();
//        ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
//        log.info("【外部接口调用】：{},响应时间：{}ms,请求参数:{},返回值:{}",
//                request.getURI(),
//                stopWatch.getTotalTimeMillis(),
//                body != null ? new String(body, "UTF-8") : null, StreamUtils.copyToString(responseCopy.getBody(), Charset.defaultCharset())
//        );
//        return responseCopy;
//    }


}