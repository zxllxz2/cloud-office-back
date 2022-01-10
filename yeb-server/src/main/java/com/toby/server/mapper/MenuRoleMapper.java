package com.toby.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toby.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 批量更新角色菜单
     * @param rid
     * @param mids
     */
    Integer insertRecords(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
