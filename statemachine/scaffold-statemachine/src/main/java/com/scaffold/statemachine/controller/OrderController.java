/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.controller;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.statemachine.entity.Order;
import com.scaffold.statemachine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hui.zhang
 * @version $Id: OrderController.java, v 0.1 2022-04-21 下午3:03 hui.zhang Exp $$
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Order create() {
        return orderService.create();
    }

    @PostMapping("/pay")
    public Order pay(@RequestBody JSONObject param) {
        return orderService.pay(param.getLong("id"));
    }

    @PostMapping("/deliver")
    public Order deliver(@RequestBody JSONObject param) {
        return orderService.deliver(param.getLong("id"));
    }

    @PostMapping("/orders")
    public Map<Long, Order> orders() {
        return orderService.getOrders();
    }
}

