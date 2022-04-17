package com.scaffold.springcloud.service.impl;

import com.scaffold.springcloud.service.HystrixService;
import org.springframework.stereotype.Component;

/**
 * @author hui.zhang
 * @date 2022年04月17日 17:23
 */
@Component
public class FallbackHystrixService implements HystrixService {
    @Override
    public String error() {
        return null;
    }

    @Override
    public String timeout(String orderNo) {
        return null;
    }

    @Override
    public String success() {
        return null;
    }
}
