package com.taohong.snapup.redis;

/**
 * @author taohong on 10/11/2018
 */
public class SnapupKey extends BasePrefix {
    public SnapupKey(String prefix) {
        super(prefix);
    }

    public static SnapupKey isGoodsOver = new SnapupKey("go");
}
