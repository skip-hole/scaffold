package com.scaffold.webmvc.controller;

import com.scaffold.webmvc.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hui.zhang
 * @date 2022年09月24日 16:59
 */

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {


    private final RestTemplate restTemplate;

    @PostMapping("/create")
    public Order create(@RequestBody Order order) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(order, header);
        return restTemplate.postForObject("http://localhost:30000/order/findById", entity, Order.class);
    }

    @PostMapping("/findById")
    public Order findById(@RequestBody Order order) {
        return new Order("1001", System.currentTimeMillis());
    }

}
