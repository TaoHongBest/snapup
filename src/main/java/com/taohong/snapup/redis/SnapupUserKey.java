package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public class SnapupUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    private SnapupUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SnapupUserKey token = new SnapupUserKey(TOKEN_EXPIRE, "tk");
    public static SnapupUserKey getById = new SnapupUserKey(0, "id");

}
