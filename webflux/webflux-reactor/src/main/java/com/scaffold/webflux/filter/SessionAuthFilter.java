package com.scaffold.webflux.filter;

import com.scaffold.webflux.exception.BusinessException;
import com.scaffold.webflux.support.SessionContext;
import com.scaffold.webflux.support.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Slf4j
public class SessionAuthFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (WebUtils.shouldNotFilter(exchange)) {
            return chain.filter(exchange);
        }

        SessionContext sessionContext = exchange.getAttribute(WebUtils.SESSION_CONTEXT_ATTR);
        if (sessionContext == null) {
            throw new BusinessException(403, "请先登录");
        }
        String userId = sessionContext.getUserId();
        log.info("userId: {}", userId);
        if (!StringUtils.hasLength(userId)) {
            throw new BusinessException(403, "请先登录");
        }
        return chain.filter(exchange);
    }
}
