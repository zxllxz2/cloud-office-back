package com.toby.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toby.server.mapper.MenuRoleMapper;
import com.toby.server.pojo.MenuRole;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //删除目前角色下所拥有权限的菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        //若mids不为空，则更新该角色的菜单权限
        if (mids == null || mids.length == 0 ) {
            return RespBean.success("Deleted successfully!");
        }
        Integer result = menuRoleMapper.insertRecords(rid, mids);
        if (result == mids.length) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.success("Fail to update all menus!");
    }
}
