package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSnapupOrderByUidGid = new OrderKey("soug");
}
