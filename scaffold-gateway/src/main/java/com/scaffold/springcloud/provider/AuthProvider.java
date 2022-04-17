package com.scaffold.springcloud.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hui.zhang
 * @date 2022年04月16日 15:19
 */
public class AuthProvider {

    private static final List<String> DEFAULT_SKIP_URL = new ArrayList<>();

    static {
        DEFAULT_SKIP_URL.add("/example");
        DEFAULT_SKIP_URL.add("/token/**");
        DEFAULT_SKIP_URL.add("/captcha/**");
        DEFAULT_SKIP_URL.add("/actuator/health/**");
        DEFAULT_SKIP_URL.add("/v2/api-docs/**");
        DEFAULT_SKIP_URL.add("/auth/**");
        DEFAULT_SKIP_URL.add("/oauth/**");
        DEFAULT_SKIP_URL.add("/log/**");
        DEFAULT_SKIP_URL.add("/menu/routes");
        DEFAULT_SKIP_URL.add("/menu/auth-routes");
        DEFAULT_SKIP_URL.add("/tenant/info");
        DEFAULT_SKIP_URL.add("/order/create/**");
        DEFAULT_SKIP_URL.add("/storage/deduct/**");
        DEFAULT_SKIP_URL.add("/error/**");
        DEFAULT_SKIP_URL.add("/assets/**");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getDefaultSkipUrl() {
        return DEFAULT_SKIP_URL;
    }
}
