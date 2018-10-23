package com.imooc.snapup.service;

import com.imooc.snapup.dao.SnapupUserDao;
import com.imooc.snapup.domain.SnapupUser;
import com.imooc.snapup.exception.GlobalException;
import com.imooc.snapup.exception.GlobalExceptionHandler;
import com.imooc.snapup.redis.RedisService;
import com.imooc.snapup.redis.SnapupUserKey;
import com.imooc.snapup.result.CodeMsg;
import com.imooc.snapup.util.MD5Util;
import com.imooc.snapup.util.UUIDUtil;
import com.imooc.snapup.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author taohong on 22/10/2018
 */
@Service
public class SnapupUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SnapupUserDao snapupUserDao;

    @Autowired
    RedisService redisService;

    public SnapupUser getById(long id) {
        return snapupUserDao.getById(id);
    }

    public SnapupUser getByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redisService.get(SnapupUserKey.token, token, SnapupUser.class);
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // Check whether phone number exists
        SnapupUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // Validate password
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
//         Generate cookie
        String token = UUIDUtil.uuid();
        redisService.set(SnapupUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SnapupUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
        return true;
    }


}
