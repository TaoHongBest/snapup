package com.taohong.snapup.service;

import com.taohong.snapup.dao.GoodsDao;
import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author taohong on 10/10/2018
 */
@Service
public class SnapupService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo snapup(SnapupUser user, GoodsVo goods) {
        // Reduce stock, make order, write snap-up order
        goodsService.reduceStock(goods);
        // order_info & snapup_order
        return orderService.createOrder(user, goods);
    }
}
