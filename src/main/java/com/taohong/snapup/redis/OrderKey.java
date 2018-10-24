package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public class OrderKey extends BasePrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
