package com.imooc.snapup.vo;

import com.imooc.snapup.domain.Goods;

import java.util.Date;

/**
 * @author taohong on 23/10/2018
 */
public class GoodsVo extends Goods {
    private Double snapupPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public Double getSnapupPrice() {
        return snapupPrice;
    }

    public void setSnapupPrice(Double snapupPrice) {
        this.snapupPrice = snapupPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
