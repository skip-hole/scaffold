/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.mapper;

import com.scaffold.dynamic.datasource.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: ProductMapper.java, v 0.1 2022-04-18 上午9:54 hui.zhang Exp $$
 */
@Mapper
public interface ProductMapper {

    List<Product> findProductList();
}
