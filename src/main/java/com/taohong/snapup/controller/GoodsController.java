package com.taohong.snapup.controller;

import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.GoodsKey;
import com.taohong.snapup.redis.KeyPrefix;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.result.Result;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsDetailVo;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * BEFORE CACHING:
     * 288 QPS at HEAP:="-Xms2048m -Xmx2048m -Xss256k" (no exception)
     * 3000 * 10
     * <p>
     * AFTER CACHING:
     * 527 QPS at HEAP:="-Xms2048m -Xmx2048m -Xss256k" (no exception)
     * 3000 * 10
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
//    @RequestMapping("/to_list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SnapupUser user) {
        model.addAttribute("user", user);
        // Get cache
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
//        return "goods_list";
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        // Render manually
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, SnapupUser user,
                          @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        // Get cache
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // Render manually
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

//        return "goods__detail";

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        // Render manually
        html = thymeleafViewResolver.getTemplateEngine().process("goods__detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, SnapupUser user,
                                        @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSnapupStatus(snapupStatus);
        return Result.success(vo);
    }
}
