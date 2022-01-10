package com.toby.server.controller;

import com.toby.server.pojo.Admin;
import com.toby.server.pojo.AdminLoginParam;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录控制器
 *
 * <p>
 * 业务逻辑：前端传用户名与密码。不需要其他信息，所以新建实体类AdminLoginParam，只用来接受用户名与密码，并且拓展灵活
 *         后端使用spring security自带的userDetailsService中的loadUserByUserName登录
 *         登录成功则返回token，前端将token置入请求头，每一次请求都会携带该请求头
 *         后端的token拦截器对每次请求进行判断，判断token是否有效；若无效则登录失效，有效才可访问其他接口
 *         因此，退出登录仅需前端判断是否收到200的状态码，收到之后从请求头中把token删除即可
 * </p>
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "Return token after login")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) {
        return adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(),
                adminLoginParam.getCode(), request);
    }

    @ApiOperation(value = "Get info of the current login user")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) {    //从全局中直接获取当前登录对象
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }


    @ApiOperation(value = "Logout")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("Logout successfully!");
    }

}
