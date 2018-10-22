package com.imooc.snapup.service;

import com.imooc.snapup.dao.SnapupUserDao;
import com.imooc.snapup.domain.SnapupUser;
import com.imooc.snapup.result.CodeMsg;
import com.imooc.snapup.util.MD5Util;
import com.imooc.snapup.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author taohong on 22/10/2018
 */
@Service
public class SnapupUserService {

    @Autowired
    SnapupUserDao snapupUserDao;

    public SnapupUser getById(long id) {
        return snapupUserDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        if (loginVo == null) {
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // Check whether phone number exists
        SnapupUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        // Check password
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
