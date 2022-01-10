package com.toby.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toby.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartments(Integer parentId);

    /**
     * 添加部门
     * @param dep
     * @return
     */
    void addDep(Department dep);

    /**
     * 删除部门
     * @param dep
     * @return
     */
    void deleteDep(Department dep);
}
