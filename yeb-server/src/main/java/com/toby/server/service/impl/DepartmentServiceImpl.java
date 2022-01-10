package com.toby.server.service.impl;

import com.toby.server.mapper.DepartmentMapper;
import com.toby.server.pojo.Department;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 获取所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    /**
     * 添加部门
     * @param dep
     * @return
     */
    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if (dep.getResult() == 1) {
            return RespBean.success("Added successfully!", dep);
        }
        return RespBean.error("Fail to add department info!");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDep(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.deleteDep(dep);
        Integer result = dep.getResult();
        if (result == -2) {
            return RespBean.error("This department contains subs. Fail to delete department info!");
        }
        if (result == -1) {
            return RespBean.error("This department contains staffs. Fail to delete department info!");
        }
        if (result == 1) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete department info!");
    }
}
