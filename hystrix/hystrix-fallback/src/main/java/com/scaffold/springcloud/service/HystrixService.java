package com.scaffold.springcloud.service;

/**
 * @author hui.zhang
 * @date 2022年04月17日 16:22
 *
 * 集成feignclient
 * @FeignClient(name = "feign-client", fallback = FallbackHystrixService.class)
 */

public interface HystrixService {

    String error();
    String timeout(String orderNo);
    String success();
}
