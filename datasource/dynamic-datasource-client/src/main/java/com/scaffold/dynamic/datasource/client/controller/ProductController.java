/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.client.controller;


import com.scaffold.dynamic.datasource.client.entity.Product;
import com.scaffold.dynamic.datasource.client.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: ProductController.java, v 0.1 2022-04-18 上午10:19 hui.zhang Exp $$
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public List<Product> list() {
        return productService.findProductList();
    }
}

