package com.scaffold.ddd.domain.model;

import com.scaffold.ddd.domain.shared.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author hui.zhang
 * @date 2022年04月23日 09:12
 */
public class Coupon implements Entity<Coupon> {

    private Long id;

    private String name;

    private String couponNo;
    /**
     * 优惠券类型 1、代金券 2、满减券 3、折扣券
     */
    private int couponType;
    /**
     * 面额
     */
    private BigDecimal couponAmount;

    private BigDecimal maxCouponAmount;
    /**
     * 投放类型 1、所有用户 2、新用户
     */
    private int grantType;

    private LocalDateTime grantStartTime;
    private LocalDateTime grantEndTime;
    private LocalDateTime effectiveStartTime;
    private LocalDateTime getEffectiveEndTime;
    private int status;
    private int quantity;
    private int repeatQuantity;

    /**
     * 使用范围 1、全场通用 2、按品牌 HW 3、品类 衣服 4 单品券
     */
    private int useType;
    /**
     * 1、手动领取 2、红包领取
     */
    private int receiveType;


    @Override
    public boolean sameIdentityAs(Coupon other) {
        return false;
    }
}
