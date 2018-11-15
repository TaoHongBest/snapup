package com.taohong.snapup.redis;

/**
 * @author taohong on 10/11/2018
 */
public class AccessKey extends BasePrefix {
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey access = new AccessKey(5, "access");
}
