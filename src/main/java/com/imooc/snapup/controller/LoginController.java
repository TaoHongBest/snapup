package com.imooc.snapup.controller;

import com.imooc.snapup.domain.User;
import com.imooc.snapup.redis.RedisService;
import com.imooc.snapup.redis.UserKey;
import com.imooc.snapup.result.CodeMsg;
import com.imooc.snapup.result.Result;
import com.imooc.snapup.service.SnapupUserService;
import com.imooc.snapup.service.UserService;
import com.imooc.snapup.util.ValidatorUtil;
import com.imooc.snapup.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.imooc.snapup.result.Result.success;
import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.T;

/**
 * @author taohong on 09/10/2018
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SnapupUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        // Login
        userService.login(loginVo);
        return Result.success(true);
    }
}