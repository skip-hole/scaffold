/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.service;

import com.scaffold.dynamic.datasource.entity.Product;

import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: ProductService.java, v 0.1 2022-04-18 上午10:03 hui.zhang Exp $$
 */
public interface ProductService {

    List<Product> findProductByName(String name);
}
