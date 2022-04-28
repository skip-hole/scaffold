/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.canal.handler;

/**
 * @author hui.zhang
 * @version $Id: MessageHandler.java, v 0.1 2022-04-27 下午4:29 hui.zhang Exp $$
 */
public interface MessageHandler<T> {

    void handleMessage(T t);
}
