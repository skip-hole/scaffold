/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.statemachine.enums;

/**
 * 订单状态
 *
 * @author hui.zhang
 * @version $Id: OrderStatus.java, v 0.1 2022-04-21 下午1:53 hui.zhang Exp $$
 */
public enum OrderStatus {

    /**
     * 待支付
     */
    WAIT_PAYMENT,
    /**
     * 待发货
     */
    WAIT_DELIVER,
    /**
     * 待收货
     */
    WAIT_RECEIVE,
    /**
     * 完成
     */
    FINISH;

}
