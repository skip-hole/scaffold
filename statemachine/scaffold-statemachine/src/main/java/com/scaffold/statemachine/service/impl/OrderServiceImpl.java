/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.service.impl;

import com.scaffold.statemachine.entity.Order;
import com.scaffold.statemachine.enums.OrderStatus;
import com.scaffold.statemachine.enums.OrderStatusChangeEvent;
import com.scaffold.statemachine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hui.zhang
 * @version $Id: OrderServiceImpl.java, v 0.1 2022-04-21 下午2:34 hui.zhang Exp $$
 */
@Service
public class OrderServiceImpl implements OrderService {

    public static final String stateMachineId = "orderStateMachineId";

    private final StateMachineFactory<OrderStatus, OrderStatusChangeEvent> orderStateMachineFactory;

    private final StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister;

    public OrderServiceImpl(StateMachineFactory<OrderStatus, OrderStatusChangeEvent> orderStateMachineFactory,
                            StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister) {
        this.orderStateMachineFactory = orderStateMachineFactory;
        this.persister = persister;
    }

    private AtomicLong id = new AtomicLong(0);
    private Map<Long, Order> orders = new ConcurrentHashMap<>();


    @Override
    public Order create() {
        Order order = new Order();
        order.setStatus(OrderStatus.WAIT_PAYMENT);
        order.setId(id.decrementAndGet());
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Order pay(Long id) {
        Order order = orders.get(id);
        System.out.println(" 等待支付 -> 等待发货 id=" + id + " threadName=" + Thread.currentThread().getName());
        Message message = MessageBuilder.withPayload(OrderStatusChangeEvent.PAID).setHeader("order", order).build();
        if (!sendEvent(message, order)) {
            System.out.println(" 等待支付 -> 等待发货 失败, 状态异常 id=" + id + " threadName=" + Thread.currentThread().getName());
        } else {
            System.out.println(" 等待支付 -> 等待发货 成功 id=" + id + " threadName=" + Thread.currentThread().getName());
        }
        return orders.get(id);
    }

    @Override
    public Order deliver(Long id) {
        Order order = orders.get(id);
        System.out.println(" 等待发货 -> 等待收货 id=" + id + " threadName=" + Thread.currentThread().getName());
        if (!sendEvent(MessageBuilder.withPayload(OrderStatusChangeEvent.DELIVERY).setHeader("order", order).build(), orders.get(id))) {
            System.out.println(" 等待发货 -> 等待收货 失败，状态异常 id=" + id + " threadName=" + Thread.currentThread().getName());
        } else {
            System.out.println(" 等待发货 -> 等待收货 成功 id=" + id + " threadName=" + Thread.currentThread().getName());
        }
        return orders.get(id);
    }

    @Override
    public Order receive(Long id) {
        Order order = orders.get(id);
        System.out.println(" 等待收货 -> 完成 收货 id=" + id + " threadName=" + Thread.currentThread().getName());
        if (!sendEvent(MessageBuilder.withPayload(OrderStatusChangeEvent.RECEIVED).setHeader("order", order).build(), orders.get(id))) {
            System.out.println(" 等待收货 -> 完成 失败，状态异常 id=" + id + " threadName=" + Thread.currentThread().getName());
        } else {
            System.out.println(" 等待收货 -> 完成 成功 id=" + id + " threadName=" + Thread.currentThread().getName());
        }
        return orders.get(id);
    }

    public Map<Long, Order> getOrders() {
        return orders;
    }

    private boolean sendEvent(Message<OrderStatusChangeEvent> message, Order order) {
        synchronized (String.valueOf(order.getId().longValue()).intern()) {
            boolean result = false;
            StateMachine<OrderStatus, OrderStatusChangeEvent> orderStateMachine = orderStateMachineFactory.getStateMachine(stateMachineId);
            System.out.println("id=" + order.getId() + " 状态机 orderStateMachine" + orderStateMachine);
            try {
                orderStateMachine.start();
                //尝试恢复状态机状态
                persister.restore(orderStateMachine, order);
                System.out.println("id=" + order.getId() + " 状态机 orderStateMachine id=" + orderStateMachine.getId());
                //添加延迟用于线程安全测试
                Thread.sleep(1000);
                result = orderStateMachine.sendEvent(message);
                //持久化状态机状态
                persister.persist(orderStateMachine, order);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                orderStateMachine.stop();
            }
            return result;

        }
    }
}

