/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.service;

import com.scaffold.statemachine.entity.Order;

import java.util.Map;

/**
 * @author hui.zhang
 * @version $Id: OrderService.java, v 0.1 2022-04-21 下午2:32 hui.zhang Exp $$
 */
public interface OrderService {

    Order create();

    Order pay(Long id);

    Order deliver(Long id);

    Order receive(Long id);

    Map<Long,Order> getOrders();
}
