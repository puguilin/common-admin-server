package com.github.guilin.controller;

import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.common.Constant;
import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.domain.vo.*;
import com.github.guilin.exception.AuthTokenException;
import com.github.guilin.service.SysUserService;
import com.github.guilin.service.impl.SysUserTokenServiceImpl;
import com.github.guilin.utils.IpUtils;
import com.github.guilin.utils.MakeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "系统用户接口")
@RestController
public class SysUserController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysUserTokenServiceImpl sysUserTokenService;
    @Resource
    private DefaultKaptcha defaultKaptcha;
    @Resource
    private SysUserService sysUserService;

    @ApiOperation("获取图片式验证码")
    @GetMapping("/api/sysUser/imageCode")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        String ip = IpUtils.getRemoteIp(request);
        String imageCode = defaultKaptcha.createText();
        String key = Constant.REDIS_IMAGE_CODE_PREFIX + ip + "_" + imageCode;
        redisTemplate.opsForValue().set(key, imageCode, 60L, TimeUnit.SECONDS);
        log.debug("Set {} [{}={}] to redis", Constant.IMAGE_CODE_NAME, key, imageCode);
        BufferedImage image = defaultKaptcha.createImage(imageCode);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
        out.close();
    }

    @ApiOperation("系统用户退出登录")
    @PostMapping("/api/sysUser/logout")
    public ResultVo logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            //检查token
            sysUserTokenService.checkToken(request, true);
            //退出登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            securityContextLogoutHandler.logout(request, response, authentication);
        } catch (AuthTokenException e) {
            return ResultVo.fail("退出登录失败！");
        }
        return ResultVo.success("退出登录成功！");
    }

    @ApiOperation("系统用户刷新token")
    @PostMapping("/api/sysUser/refreshToken")
    public ResultVo refreshToken(HttpServletRequest request) {
        try {
            //检查token
            sysUserTokenService.checkToken(request, true);
            //获取token
            String accessToken = request.getParameter(Constant.REFRESH_TOKEN_NAME);
            if (StringUtils.isEmpty(accessToken)) {
                return ResultVo.fail("请传入refreshToken！");
            } else {
                accessToken = accessToken.replace(Constant.TOKEN_PREFIX, "");
            }
            //刷新token
            TokenVo tokenVo = sysUserTokenService.refreshToken(accessToken);
            //返回封装值
            return ResultVo.successWith(tokenVo);
        } catch (AuthTokenException e) {
            return ResultVo.fail("刷新token失败！");
        }
    }

    @ApiOperation("获取用户基本信息")
    @GetMapping("/api/sysUser/getInfo")
    public ResultVo getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        SysUserInfoVo sysUserInfoVo = new SysUserInfoVo();
        sysUserInfoVo.setId(sysUser.getId());
        sysUserInfoVo.setName(sysUser.getUsername());
        sysUserInfoVo.setAvatar(sysUser.getAvatar());
        sysUserInfoVo.setRoles(sysUser.getMenus().stream().map(item -> item.getCode()).toArray());
        return ResultVo.successWith(sysUserInfoVo);
    }

    @ApiOperation("获取前端路由列表")
    @GetMapping("/api/sysUser/getRouterList")
    public ResultVo getRouterist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        List<SysMenu> menus = sysUser.getMenus();
        List<SysMenu> menuList = menus.stream().filter(item -> !item.getType().equals(2)).collect(Collectors.toList());
        List<RouterVo> routerVoList = MakeUtils.makeRouterTree(menuList, 0L);
        return ResultVo.successWith(routerVoList);
    }

    @SysLogRecord("新增系统用户信息")
    @PreAuthorize("hasAuthority('sys:user:add')")
    @ApiOperation("新增系统用户信息")
    @PostMapping("/api/sysUser/add")
    public ResultVo add(@RequestBody SysUser sysUser) {
        int rows = sysUserService.add(sysUser);
        if (rows > 0) {
            return ResultVo.success("添加成功！");
        } else {
            return ResultVo.fail("添加失败！");
        }
    }

    @SysLogRecord("根据主键删除用户")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @ApiOperation("根据主键删除用户")
    @GetMapping("/api/sysUser/deleteById")
    public ResultVo deleteById(Long id) {
        int rows = sysUserService.deleteById(id);
        if (rows > 0) {
            return ResultVo.success("更新成功！");
        } else {
            return ResultVo.fail("更新失败！");
        }
    }

    @SysLogRecord("更新系统用户信息")
    @PreAuthorize("hasAuthority('sys:user:update')")
    @ApiOperation("更新系统用户信息")
    @PostMapping("/api/sysUser/update")
    public ResultVo updateById(@RequestBody SysUser sysUser) {
        int rows = sysUserService.updateById(sysUser);
        if (rows > 0) {
            return ResultVo.success("更新成功！");
        } else {
            return ResultVo.fail("更新失败！");
        }
    }

    @SysLogRecord("根据主键获取用户")
    @PreAuthorize("hasAuthority('sys:user:update')")
    @ApiOperation("根据主键获取用户")
    @GetMapping("/api/sysUser/getById")
    public ResultVo getById(Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return ResultVo.successWith(sysUser);
    }

    @SysLogRecord("分页获取系统用户")
    @ApiOperation("分页获取系统用户")
    @GetMapping("/api/sysUser/getPage")
    public ResultVo getPage(SysUser query, Integer pageNum, Integer pageSize) {
        PageInfo<SysUser> sysUserPage = sysUserService.getPage(query, pageNum, pageSize);
        return ResultVo.successWith(sysUserPage);
    }

    @SysLogRecord("根据用户获取角色")
    @PreAuthorize("hasAuthority('sys:user:assign')")
    @ApiOperation("根据用户获取角色")
    @GetMapping("/api/sysUser/getRole")
    public ResultVo getRole(Long id, Integer pageSize) {
        RoleVo roleVo = sysUserService.getRole(id, pageSize);
        return ResultVo.successWith(roleVo);
    }

    @SysLogRecord("指定用户分配角色")
    @PreAuthorize("hasAuthority('sys:user:assign')")
    @ApiOperation("指定用户分配角色")
    @GetMapping("/api/sysUser/assignRole")
    public ResultVo assignRole(Long uid, @RequestParam(value = "rids[]", required = false) List<Long> rids) {
        int rows = sysUserService.assignRole(uid, rids);
        if (rows > 0) {
            return ResultVo.success("分配角色成功！");
        } else {
            return ResultVo.fail("分配角色失败！");
        }
    }

    @SysLogRecord("根据名称获取信息")
    @ApiOperation("根据名称获取信息")
    @GetMapping("/api/sysUser/getByUsername")
    public ResultVo getByUsername(String username) {
        return ResultVo.successWith(sysUserService.getByUsername(username));
    }

    @SysLogRecord("修改系统用户密码")
    @ApiOperation("修改系统用户密码")
    @GetMapping("/api/sysUser/changePassword")
    public ResultVo changePassword(String username, String oldPassword, String newPassword, String againPassword) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(againPassword)) {
            return ResultVo.fail("非法修改密码！");
        }
        if (!newPassword.equals(againPassword)) {
            return ResultVo.fail("再次确认密码！");
        }
        boolean result = sysUserService.changePassword(username, oldPassword, newPassword);
        if (result) {
            return ResultVo.success("修改密码成功！");
        } else {
            return ResultVo.fail("修改密码失败！");
        }
    }
}
