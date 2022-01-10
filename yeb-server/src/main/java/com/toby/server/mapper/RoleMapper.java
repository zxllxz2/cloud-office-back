package com.toby.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toby.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
