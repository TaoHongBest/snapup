package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
