/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * @author hui.zhang
 * @version $Id: DataSource.java, v 0.1 2022-04-18 下午7:34 hui.zhang Exp $$
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     *
     * @return the database you want to switch
     */
    String value();
}
