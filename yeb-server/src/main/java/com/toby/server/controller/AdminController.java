package com.toby.server.controller;


import com.toby.server.pojo.Admin;
import com.toby.server.pojo.RespBean;
import com.toby.server.pojo.Role;
import com.toby.server.service.IAdminService;
import com.toby.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation("Get all admin staffs")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords) {
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation("Update admin staffs")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (adminService.updateById(admin)) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update admin info!");
    }

    @ApiOperation("Delete admin staffs")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete admin info!");
    }

    @ApiOperation("Get all admin roles")
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation("Update admin roles")
    @PutMapping("/role")
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        return adminService.updateAdminRole(adminId, rids);
    }

}
