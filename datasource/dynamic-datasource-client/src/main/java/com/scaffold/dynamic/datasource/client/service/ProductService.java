/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.client.service;

import com.scaffold.dynamic.datasource.client.entity.Product;

import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: ProductService.java, v 0.1 2022-04-18 上午10:03 hui.zhang Exp $$
 */
public interface ProductService {

    List<Product> findProductList();
}
