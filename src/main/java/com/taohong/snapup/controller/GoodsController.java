package com.taohong.snapup.controller;

import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author taohong on 09/10/2018
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SnapupUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model, SnapupUser user) {
        model.addAttribute("user", user);
        // Query goods list
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SnapupUser user,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int snapupStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {// Snap-up has not started. Countdown
            snapupStatus = 0;
            remainSeconds = (int) (startAt - now) / 1000;
        } else if (now > endAt) { // Snap-up has ended.
            snapupStatus = 2;
            remainSeconds = -1;
        } else { // Snapping up!
            snapupStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("snapupStatus", snapupStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods__detail";
    }
}
