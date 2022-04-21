/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.entity;

import com.scaffold.statemachine.enums.OrderStatus;
import lombok.Data;

/**
 * @author hui.zhang
 * @version $Id: Order.java, v 0.1 2022-04-21 下午2:26 hui.zhang Exp $$
 */
@Data
public class Order {

    private Long id;
    private OrderStatus status;

}

