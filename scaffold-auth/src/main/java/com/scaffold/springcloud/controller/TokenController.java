package com.scaffold.springcloud.controller;

import com.scaffold.springcloud.domain.Result;
import com.scaffold.springcloud.form.LoginBody;
import com.scaffold.springcloud.form.LoginUser;
import com.scaffold.springcloud.service.TokenService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hui.zhang
 * @date 2022年04月17日 08:25
 */
@RestController
public class TokenController {


    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("login")
    public Result<?> login(@RequestBody LoginBody form) {
        // 获取用户登录
        // 获取登录token
        return Result.success(tokenService.createToken(new LoginUser()));
    }

    @DeleteMapping("logout")
    public Result<?> logout(HttpServletRequest request) {
        //删除用户登录缓存信息
        return Result.success(null);
    }

    @PostMapping("refresh")
    public Result<?> refresh(HttpServletRequest request) {
        // 刷新令牌有效期
        tokenService.refreshToken(new LoginUser());
        return Result.success(null);
    }

    @PostMapping("register")
    public Result<?> register() {
        // 用户注册
        return Result.success(null);
    }
}
