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
 * @version $Id: PayOrderStateListener.java, v 0.1 2022-04-21 下午4:00 hui.zhang Exp $$
 */
@Component
public class PayOrderStateListener implements OrderStateListener {

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    @Override
    public boolean transition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.WAIT_DELIVER);
        System.out.println("支付 headers=" + message.getHeaders().toString());
        return true;
    }
}

