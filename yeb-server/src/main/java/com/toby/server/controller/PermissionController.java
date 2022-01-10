package com.toby.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toby.server.pojo.Menu;
import com.toby.server.pojo.MenuRole;
import com.toby.server.pojo.RespBean;
import com.toby.server.pojo.Role;
import com.toby.server.service.IMenuRoleService;
import com.toby.server.service.IMenuService;
import com.toby.server.service.IRoleService;
import com.toby.server.service.impl.MenuRoleServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "Get all roles")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation(value = "Add role")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        if (!role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)) {
            return RespBean.success("Added successfully!");
        }
        return RespBean.error("Fail to add role!");
    }

    @ApiOperation(value = "delete role")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid) {
        if (roleService.removeById(rid)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete role!");
    }

    @ApiOperation(value = "Search all menus")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "Search menu id according to role id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid) {
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "Update role menu")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        return menuRoleService.updateMenuRole(rid, mids);
    }
}
