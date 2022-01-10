package com.toby.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.toby.server.pojo.MenuRole;
import com.toby.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
