package com.taohong.snapup.redis;

/**
 * @author taohong on 18/10/2018
 */
public abstract class BasePrefix implements KeyPrefix {
    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) { // Default 0 means never expire
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String  getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
