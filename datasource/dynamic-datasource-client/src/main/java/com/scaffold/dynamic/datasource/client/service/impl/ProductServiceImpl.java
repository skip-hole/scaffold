/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.client.service.impl;

import com.scaffold.dynamic.datasource.annotation.DS;
import com.scaffold.dynamic.datasource.client.entity.Product;
import com.scaffold.dynamic.datasource.client.mapper.ProductMapper;
import com.scaffold.dynamic.datasource.client.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: ProductServiceImpl.java, v 0.1 2022-04-18 上午10:04 hui.zhang Exp $$
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @DS("slave")
    @Override
    public List<Product> findProductList() {
        return productMapper.findProductList();
    }
}

