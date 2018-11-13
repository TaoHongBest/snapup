package com.taohong.snapup.redis;

/**
 * @author taohong on 10/11/2018
 */
public class SnapupKey extends BasePrefix {
    private SnapupKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SnapupKey isGoodsOver = new SnapupKey(0, "go");
    public static SnapupKey getSnapupPath = new SnapupKey(60, "sp");

}
