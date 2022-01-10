package com.toby.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toby.server.AdminUtils;
import com.toby.server.config.security.component.JwtTokenUtil;
import com.toby.server.mapper.AdminMapper;
import com.toby.server.mapper.AdminRoleMapper;
import com.toby.server.mapper.RoleMapper;
import com.toby.server.pojo.Admin;
import com.toby.server.pojo.AdminRole;
import com.toby.server.pojo.RespBean;
import com.toby.server.pojo.Role;
import com.toby.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;    //ignore error

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {

        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(captcha) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("Captcha is incorrect. Please enter it again!");
        }

        //登录判断
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) return RespBean.error("No such account registered!");

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("Username or password is incorrect!");
        }

        if (!userDetails.isEnabled()) {
            return RespBean.error("Your account is disabled. Please contact the admin!");
        }

        //更新security登录用户对象，将其放入security全局
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return RespBean.success("Login successful!", tokenMap);
    }

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
//                                                              .eq("enabled", true));
    }

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {

        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        if (rids == null || rids.length == 0 ) {
            return RespBean.success("Deleted successfully!");
        }
        Integer result = adminRoleMapper.updateAdminRole(adminId, rids);
        if (rids.length == result) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update admin info!");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if (encoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (result == 1) {
                return RespBean.success("Password is updated successfully!");
            }
        }
        return RespBean.error("Fail to update the password!");
    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param auth
     * @return
     */
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication auth) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (result == 1) {
            Admin principal = (Admin) auth.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin, null, auth.getAuthorities()));
            return RespBean.success("Updated successfully", url);
        }
        return RespBean.error("Fail to update the avatar!");
    }

}
