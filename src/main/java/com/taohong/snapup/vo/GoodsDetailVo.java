package com.taohong.snapup.vo;

import com.taohong.snapup.domain.SnapupUser;

/**
 * @author taohong on 26/11/2018
 */
public class GoodsDetailVo {
    private int snapupStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SnapupUser user;

    public int getSnapupStatus() {
        return snapupStatus;
    }

    public void setSnapupStatus(int snapupStatus) {
        this.snapupStatus = snapupStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public SnapupUser getUser() {
        return user;
    }

    public void setUser(SnapupUser user) {
        this.user = user;
    }
}
