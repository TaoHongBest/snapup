package com.imooc.snapup.util;

import java.util.UUID;

/**
 * @author taohong on 22/10/2018
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
