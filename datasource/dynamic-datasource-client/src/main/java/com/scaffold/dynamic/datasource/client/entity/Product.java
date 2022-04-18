/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.client.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hui.zhang
 * @version $Id: Product.java, v 0.1 2022-04-18 上午9:58 hui.zhang Exp $$
 */
@Data
public class Product {

    private int id;
    private String name;
    private BigDecimal price;
}

