/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.ddd.domain.shared;

/**
 * 优惠券类型
 *
 * @author hui.zhang
 * @version $Id: CouponTypeEnum.java, v 0.1 2022-04-24 下午3:43 hui.zhang Exp $$
 */
public enum CouponTypeEnum {

    FULL_REDUCE_COUPON(1, "满减券"),
    ITEM_COUPON(2, "单品券"),
    CATEGORY_COUPON(3, "品类券"),
    BRAND_COUPON(4, "品牌券");

    private final int type;
    private final String desc;

    CouponTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
