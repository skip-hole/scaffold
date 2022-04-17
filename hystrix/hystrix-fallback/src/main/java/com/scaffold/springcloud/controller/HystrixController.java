package com.scaffold.springcloud.controller;

import com.scaffold.springcloud.service.HystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hui.zhang
 * @date 2022年04月17日 16:18
 */
@Slf4j
@RestController
public class HystrixController {


    private final HystrixService hystrixService;

    public HystrixController(HystrixService hystrixService) {
        this.hystrixService = hystrixService;
    }

    @GetMapping("/fallback")
    public String error() {
        return hystrixService.error();
    }

    @GetMapping("/timeout")
    public String timeout() {
        return hystrixService.timeout("111");
    }

}
