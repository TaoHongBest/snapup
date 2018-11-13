package com.taohong.snapup.service;

import com.taohong.snapup.dao.GoodsDao;
import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.redis.SnapupKey;
import com.taohong.snapup.util.MD5Util;
import com.taohong.snapup.util.UUIDUtil;
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

    public boolean checkPath(SnapupUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(SnapupKey.getSnapupPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);

    }

    public String createSnapupPath(SnapupUser user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SnapupKey.getSnapupPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }
}
