package com.scaffold.springcloud.service;

import com.google.common.base.Joiner;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
public abstract class BaseRemoteService {

    public void baseFallBack(String commandKey, Throwable e, String desc, Object... objects) {
        HystrixCommandKey hystrixCommandKey = HystrixCommandKey.Factory.asKey(commandKey);
        HystrixCircuitBreaker hystrixCircuitBreaker = HystrixCircuitBreaker.Factory.getInstance(hystrixCommandKey);
        boolean open = hystrixCircuitBreaker.isOpen();
        HystrixCommandMetrics hystrixCommandMetrics = HystrixCommandMetrics.getInstance(hystrixCommandKey);
        HystrixCommandMetrics.HealthCounts healthCounts = hystrixCommandMetrics.getHealthCounts();
        long errorCount = healthCounts.getErrorCount() + 1;
        long totalRequests = healthCounts.getTotalRequests() + 1;
        int errorPercentage = (int) ((double) errorCount / totalRequests * 100);
        String message;
        String content;
        if (e instanceof HystrixTimeoutException) {
            message = "已超过此接口最大响应时间。";
        } else if (e instanceof RejectedExecutionException) {
            message = "已超过此接口最大并发数,进行限流降级处理。";
        } else {
            message = e != null ? e.toString() : null;
        }
        String params = Joiner.on(",").join(Arrays.asList(objects));
        log.info("参数{},调用{},异常原因{}", params, desc, message);
        if (open) {
            content = this.getContent(StringUtils.join("熔断器已打开,暂时停止调用", desc, "5秒后尝试自动恢复。"), null);
        } else {
            content = this.getContent(StringUtils.join(desc, "最近10秒内总请求数:", totalRequests, ",失败次数：", errorCount, ",失败百分比：", errorPercentage, "%"), StringUtils.join(message, "参数:", params));

        }
        log.error(content);
    }


    public String getContent(String content, String errorMessage) {
        return errorMessage != null ? StringUtils.join(content, "异常原因：", errorMessage) : content;
    }

    /**
     * toString方法
     *
     * @param object
     * @return
     */
    public String toSting(Object object) {
        return Objects.toString(object);
    }
}
