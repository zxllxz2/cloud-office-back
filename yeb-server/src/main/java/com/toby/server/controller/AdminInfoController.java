package com.toby.server.controller;

import com.toby.server.pojo.Admin;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 个人中心
 */
@RestController
@RequestMapping
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

//    private final Path AVATAR_FOLDER_PATH = Paths.get("yeb-server/src/main/resources/assets");

    @ApiOperation(value = "Update info of the current user")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateById(admin)) {
            //security全局上下文中重新设置authentication对象
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin, null,
                             authentication.getAuthorities()));

            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update the info!");
    }

    @ApiOperation(value = "Update user's password")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }

    ///TODO: Use FastDFS to make image uploading
    @ApiOperation(value = "Update user's avatar")
    @PostMapping("/admin/userface")
    public RespBean updateAdminUserFace(String url, Integer id, Authentication auth) {
        return adminService.updateAdminUserFace(url, id, auth);
    }

}
