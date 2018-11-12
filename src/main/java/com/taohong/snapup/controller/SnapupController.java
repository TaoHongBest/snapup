package com.taohong.snapup.controller;

import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.rabbitmq.MQSender;
import com.taohong.snapup.rabbitmq.SnapupMessage;
import com.taohong.snapup.redis.GoodsKey;
import com.taohong.snapup.redis.OrderKey;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.redis.SnapupKey;
import com.taohong.snapup.result.CodeMsg;
import com.taohong.snapup.result.Result;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.OrderService;
import com.taohong.snapup.service.SnapupService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author taohong on 09/10/2018
 */
@Controller
@RequestMapping("/snapup")
public class SnapupController implements InitializingBean {

    @Autowired
    SnapupUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SnapupService snapupService;

    @Autowired
    MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSnapupGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getSnapupGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSnapupOrderByUidGid);
        redisService.delete(SnapupKey.isGoodsOver);
        snapupService.reset(goodsList);
        return Result.success(true);
    }


    /**
     * 351 QPS at HEAP:="-Xms2048m -Xmx2048m -Xss256k" (order created & no exception)
     * 3000 * 10
     * <p>
     * 599 QPS after rabbitmq optimization
     */
    @RequestMapping(value = "/do_snapup", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> snapup(Model model, SnapupUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // Memory mark, reducing times to access redis
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SNAPUP_OVER);
        }

        // Pre-reduce stock
        long stock = redisService.decr(GoodsKey.getSnapupGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SNAPUP_OVER);
        }

        // Check if have snapped up an item
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SNAPUP);
        }
        // Enqueue
        SnapupMessage sm = new SnapupMessage();
        sm.setUser(user);
        sm.setGoodsId(goodsId);
        sender.sendSnapupMessage(sm);
        return Result.success(0); // 0 means queueing

        /*
        // Check stock
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.SNAPUP_OVER);
        }

        // Check if have snapped up an item
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SNAPUP);
        }


        return Result.success(orderInfo);
        */
    }

    /**
     * orderId (success)
     * -1 (failure)
     * 0 (queueing)
     */

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> snapupResult(Model model, SnapupUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = snapupService.getSnapupResult(user.getId(), goodsId);
        return Result.success(result);
    }
}