package com.taohong.snapup.service;

import com.taohong.snapup.dao.OrderDao;
import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.OrderKey;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author taohong on 24/10/2018
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public SnapupOrder getSnapupOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderDao.getSnapupOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getSnapupOrderByUidGid, "" + userId + "_" + goodsId, SnapupOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(SnapupUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSnapupPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        SnapupOrder snapupOrder = new SnapupOrder();
        snapupOrder.setGoodsId(goods.getId());
        snapupOrder.setOrderId(orderInfo.getId());
        snapupOrder.setUserId(user.getId());
        orderDao.insertSnapupOrder(snapupOrder);

        redisService.set(OrderKey.getSnapupOrderByUidGid, "" + user.getId() + "_" + goods.getId(), snapupOrder);
        return orderInfo;
    }


    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteSnapupOrders();
    }
}
