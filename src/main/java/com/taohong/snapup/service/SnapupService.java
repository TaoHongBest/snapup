package com.taohong.snapup.service;

import com.taohong.snapup.dao.GoodsDao;
import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.redis.SnapupKey;
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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo snapup(SnapupUser user, GoodsVo goods) {
        // Reduce stock, make order, write snap-up order
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            // order_info & snapup_order
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getSnapupResult(Long userId, long goodsId) {
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SnapupKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SnapupKey.isGoodsOver, "" + goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }
}
