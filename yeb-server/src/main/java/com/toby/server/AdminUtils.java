package com.toby.server;

import com.toby.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Admin工具类
 */
public class AdminUtils {

    /**
     * 获取当前登录操作员
     *
     * @return
     */
    public static Admin getCurrentAdmin() {
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
