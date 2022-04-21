/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.configuration;

import com.scaffold.statemachine.entity.Order;
import com.scaffold.statemachine.enums.OrderStatus;
import com.scaffold.statemachine.enums.OrderStatusChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * 订单机状态配置
 *
 * @author hui.zhang
 * @version $Id: OrderStateMachineConfigure.java, v 0.1 2022-04-21 下午2:01 hui.zhang Exp $$
 */
@Configuration
@EnableStateMachineFactory(name = "orderStateMachineFactory")
public class OrderStateMachineConfigurer extends StateMachineConfigurerAdapter<OrderStatus, OrderStatusChangeEvent> {


    public static final String orderStateMachineId = "orderStateMachineId";

    /**
     * 配置状态
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderStatusChangeEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    /**
     * 配置状态事件转换关系
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderStatusChangeEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatusChangeEvent.PAID.getSource()).target(OrderStatusChangeEvent.PAID.getTarget()).event(OrderStatusChangeEvent.PAID)
                .and()
                .withExternal().source(OrderStatusChangeEvent.DELIVERY.getSource()).target(OrderStatusChangeEvent.DELIVERY.getTarget()).event(OrderStatusChangeEvent.DELIVERY)
                .and()
                .withExternal().source(OrderStatusChangeEvent.RECEIVED.getSource()).target(OrderStatusChangeEvent.RECEIVED.getTarget()).event(OrderStatusChangeEvent.RECEIVED);
    }

    /**
     * 持久化配置
     * 实际使用中，可以配合redis等，进行持久化操作
     * @return
     */
    @Bean
    public StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister(){
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderStatus, OrderStatusChangeEvent, Order>() {
            @Override
            public void write(StateMachineContext<OrderStatus, OrderStatusChangeEvent> context, Order order) throws Exception {
                //此处并没有进行持久化操作
                order.setStatus(context.getState());
            }

            @Override
            public StateMachineContext<OrderStatus, OrderStatusChangeEvent> read(Order order) throws Exception {
                //此处直接获取order中的状态，其实并没有进行持久化读取操作
                StateMachineContext<OrderStatus, OrderStatusChangeEvent> result =new DefaultStateMachineContext<>(order.getStatus(), null, null, null, null, orderStateMachineId);
                return result;
            }
        });
    }
}

