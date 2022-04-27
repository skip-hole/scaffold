/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.datasync.handler;

/**
 * @author hui.zhang
 * @version $Id: EntryHandler.java, v 0.1 2022-04-27 下午4:39 hui.zhang Exp $$
 */
public interface EntryHandler<T> {
    default void insert(T t) {
    }

    default void update(T before, T after) {
    }

    default void delete(T t) {
    }

}
