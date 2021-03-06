package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public class GoodsKey extends BasePrefix {
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(1, "gl");
    public static GoodsKey getGoodsDetail= new GoodsKey(60, "gd");
    public static GoodsKey getSnapupGoodsStock= new GoodsKey(0, "gs");
}
