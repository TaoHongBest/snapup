package com.taohong.snapup.controller;

import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.result.CodeMsg;
import com.taohong.snapup.result.Result;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.OrderService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsVo;
import com.taohong.snapup.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author taohong on 26/11/2018
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    SnapupUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> list(Model model, SnapupUser user,
                                      @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);

        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
