package com.taohong.snapup.rabbitmq;

import com.taohong.snapup.domain.SnapupUser;

/**
 * @author taohong on 10/11/2018
 */
public class SnapupMessage {
    private SnapupUser user;
    private long goodsId;

    public SnapupUser getUser() {
        return user;
    }

    public void setUser(SnapupUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
