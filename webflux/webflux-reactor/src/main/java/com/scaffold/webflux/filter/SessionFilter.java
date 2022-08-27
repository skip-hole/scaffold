package com.scaffold.webflux.filter;

import com.scaffold.webflux.support.SessionContext;
import com.scaffold.webflux.support.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
public class SessionFilter implements WebFilter {

    private static final String COOKIE_NAME = "SESSIONID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (WebUtils.shouldNotFilter(exchange)) {
            return chain.filter(exchange);
        }
        List<HttpCookie> httpCookies = exchange.getRequest().getCookies().get(COOKIE_NAME);
        String userId;
        if (!CollectionUtils.isEmpty(httpCookies)) {
            String sessionId = httpCookies.get(0).getValue();
            userId = getUserIdBySessionId(sessionId);

        } else {
            // just for test
            userId = exchange.getRequest().getQueryParams().getFirst("userId");
        }
        if (StringUtils.hasLength(userId)) {
            SessionContext sessionContext = new SessionContext();
            sessionContext.setUserId(userId);
            exchange.getAttributes().put(WebUtils.SESSION_CONTEXT_ATTR, sessionContext);
        }
        return chain.filter(exchange);
    }

    private String getUserIdBySessionId(String sessionId) {
        return null;
    }
}
