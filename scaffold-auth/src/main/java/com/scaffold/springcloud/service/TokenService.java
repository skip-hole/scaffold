package com.scaffold.springcloud.service;

import com.scaffold.springcloud.constant.SecurityConstants;
import com.scaffold.springcloud.form.LoginUser;
import com.scaffold.springcloud.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author hui.zhang
 * @date 2022年04月17日 08:26
 */
@Service
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = 720;

    private final static String ACCESS_TOKEN = "login_tokens";

    private final static Long MILLIS_MINUTE_TEN = 120 * MILLIS_MINUTE;

    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        Long userId = loginUser.getUserid();
        String userName = loginUser.getUsername();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        //TODO 登录信息放入redis
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}
