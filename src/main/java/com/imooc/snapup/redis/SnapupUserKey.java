package com.imooc.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public class SnapupUserKey extends BasePrefix {
    private SnapupUserKey(String prefix) {
        super(prefix);
    }

    public static SnapupUserKey token = new SnapupUserKey("tk");
}
