/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.listener;

import com.scaffold.statemachine.configuration.OrderStateMachineConfigurer;
import com.scaffold.statemachine.enums.OrderStatusChangeEvent;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * @author hui.zhang
 * @version $Id: OrderStateListener.java, v 0.1 2022-04-21 下午3:58 hui.zhang Exp $$
 */
@WithStateMachine(id = OrderStateMachineConfigurer.orderStateMachineId)
public interface OrderStateListener {

    boolean transition(Message<OrderStatusChangeEvent> message);
}
