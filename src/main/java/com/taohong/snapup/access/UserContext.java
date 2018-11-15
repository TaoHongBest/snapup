package com.taohong.snapup.access;

import com.taohong.snapup.domain.SnapupUser;

/**
 * @author taohong on 15/11/2018
 */
public class UserContext {
    // ThreadLocal: binding with thread, securing the thread
    private static ThreadLocal<SnapupUser> userHolder = new ThreadLocal<SnapupUser>();

    public static void setUser(SnapupUser user) {
        userHolder.set(user);
    }

    public static SnapupUser getUser() {
        return userHolder.get();
    }

}
