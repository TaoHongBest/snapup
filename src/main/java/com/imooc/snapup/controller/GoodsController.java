package com.imooc.snapup.controller;

import com.imooc.snapup.domain.SnapupUser;
import com.imooc.snapup.redis.RedisService;
import com.imooc.snapup.redis.SnapupUserKey;
import com.imooc.snapup.result.Result;
import com.imooc.snapup.service.SnapupUserService;
import com.imooc.snapup.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @RequestMapping("/to_list")
    public String list(Model model, SnapupUser user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
