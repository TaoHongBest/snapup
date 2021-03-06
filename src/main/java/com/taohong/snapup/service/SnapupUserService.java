package com.taohong.snapup.service;

import com.taohong.snapup.dao.SnapupUserDao;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.exception.GlobalException;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.redis.SnapupUserKey;
import com.taohong.snapup.result.CodeMsg;
import com.taohong.snapup.util.MD5Util;
import com.taohong.snapup.util.UUIDUtil;
import com.taohong.snapup.vo.LoginVo;
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
        // Get cache
        SnapupUser user = redisService.get(SnapupUserKey.getById, "" + id, SnapupUser.class);
        if (user != null) {
            return user;
        }
        // Retrieve database
        user = snapupUserDao.getById(id);
        if (user != null) {
            redisService.set(SnapupUserKey.getById, "" + id, user); // Cache user into database. No need to retrieve database again.
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        // Get user
        SnapupUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // Update database
        SnapupUser toBeUpdate = new SnapupUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        snapupUserDao.update(toBeUpdate);
        // Process cache
        redisService.delete(SnapupUserKey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SnapupUserKey.token, token, user);
        return true;
    }


    public SnapupUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SnapupUser user = redisService.get(SnapupUserKey.token, token, SnapupUser.class);
        // Extend period of validity
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
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
        // Generate cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, SnapupUser user) {
        redisService.set(SnapupUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SnapupUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
