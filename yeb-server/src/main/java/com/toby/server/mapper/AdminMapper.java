package com.toby.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toby.server.pojo.Admin;
import com.toby.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有操作员
     *
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);
}
