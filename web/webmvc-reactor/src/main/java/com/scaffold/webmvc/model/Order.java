package com.scaffold.webmvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {

    private String id;

    private Long start;

    public Order() {
    }

    public Order(String id) {
        this.id = id;
    }
}
