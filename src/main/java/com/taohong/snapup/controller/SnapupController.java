package com.taohong.snapup.controller;

import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.result.CodeMsg;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.OrderService;
import com.taohong.snapup.service.SnapupService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author taohong on 09/10/2018
 */
@Controller
@RequestMapping("/snapup")
public class SnapupController {

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

    @RequestMapping("/do_snapup")
    public String list(Model model, SnapupUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }

        // Check stock
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.SNAPUP_OVER.getMsg());
            return "snapup_fail";
        }

        // Check if have snapped up an item
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEAT_SNAPUP.getMsg());
            return "snapup_fail";
        }

        // Reduce stock, make order, write snap-up order
        OrderInfo orderInfo = snapupService.snapup(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
