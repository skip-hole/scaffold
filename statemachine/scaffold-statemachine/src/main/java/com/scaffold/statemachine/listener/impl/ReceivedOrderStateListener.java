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
 * @version $Id: ReceivedOrderStateListener.java, v 0.1 2022-04-21 下午4:10 hui.zhang Exp $$
 */
@Component
public class ReceivedOrderStateListener  implements OrderStateListener {

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    @Override
    public boolean transition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.FINISH);
        System.out.println("发货 headers=" + message.getHeaders().toString());
        return true;
    }
}

