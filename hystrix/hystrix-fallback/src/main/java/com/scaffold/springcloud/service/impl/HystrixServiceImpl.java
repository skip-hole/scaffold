package com.scaffold.springcloud.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.scaffold.springcloud.service.BaseRemoteService;
import com.scaffold.springcloud.service.HystrixService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hui.zhang
 * @date 2022年04月17日 16:26
 */
@Service
public class HystrixServiceImpl extends BaseRemoteService implements HystrixService {


    @HystrixCommand(fallbackMethod = "fallbackError")
    @Override
    public String error() {
        throw new RuntimeException("error");
    }


    @GetMapping("/timeout")
    @HystrixCommand(ignoreExceptions = {HystrixBadRequestException.class},
            fallbackMethod = "fallbackTimeout", threadPoolKey = "TIME_OUT",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "100")
            }, threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "50")})
    @Override
    public String timeout(String orderNo) {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {

        }
        return "timeout";

    }

    @Override
    public String success() {
        return "success";
    }

    public String fallbackError() {
        return "fallback error";
    }

    public String fallbackTimeout(String orderNo, Throwable e) {
        super.baseFallBack("timeout", e, "还款计划", new Object[]{super.toSting(orderNo)});
        return "timeout timeoutFallback";
    }
}
