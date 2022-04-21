/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.enums;

/**
 * @author hui.zhang
 * @version $Id: OrderEventStatus.java, v 0.1 2022-04-21 下午3:32 hui.zhang Exp $$
 */
public enum OrderStatusChangeEvent {
    PAID(OrderStatus.WAIT_PAYMENT, OrderStatus.WAIT_DELIVER),
    DELIVERY(OrderStatus.WAIT_DELIVER, OrderStatus.WAIT_RECEIVE),
    RECEIVED(OrderStatus.WAIT_RECEIVE, OrderStatus.FINISH);

    private final OrderStatus source;
    private final OrderStatus target;

    OrderStatusChangeEvent(OrderStatus source, OrderStatus target) {
        this.source = source;
        this.target = target;
    }

    public OrderStatus getSource() {
        return source;
    }

    public OrderStatus getTarget() {
        return target;
    }
}
