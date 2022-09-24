package com.scaffold.reactive.wiretap.webclient;

import java.util.Map;

/**
 * WebClient 请求响应获取埋点
 *
 * @author hui.zhang
 * <p>
 * return WebClient.builder()
 * .filter(new MetricWebClientFilter()) 添加自定义过滤器 new MetricWebClientFilter()
 * .clientConnector(new ReactorClientHttpConnector(httpClient))
 * .build();
 * }
 * @date 2022年09月07日 10:02
 */
public class WebClientFinishWiretap {

    public Map<String, Object> finish(Map<String, Object> request, Map<String, Object> response) {
        return response;
    }

}
