package com.scaffold.webflux.dao;

import com.scaffold.webflux.model.Order;
import reactor.core.publisher.Mono;

public interface OrderRepository {

    Mono<Order> findById(String id);

    Mono<Order> save(Order order);

    Mono<Order> deleteById(String id);
}
