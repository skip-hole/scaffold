/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.listener.impl;

import com.scaffold.statemachine.entity.Order;
import com.scaffold.statemachine.enums.OrderStatus;
import com.scaffold.statemachine.enums.OrderStatusChangeEvent;
import com.scaffold.statemachine.listener.OrderStateListener;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.stereotype.Component;

/**
 * @author hui.zhang
 * @version $Id: DeliveryOrderStateListener.java, v 0.1 2022-04-21 下午4:08 hui.zhang Exp $$
 */
@Component
public class DeliveryOrderStateListener implements OrderStateListener {

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    @Override
    public boolean transition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.WAIT_RECEIVE);
        System.out.println("发货 headers=" + message.getHeaders().toString());
        return true;
    }
}

