package com.scaffold.reactive.enhance.webflux;


import com.scaffold.reactive.util.ContextUtils;
import com.scaffold.reactive.wiretap.webflux.WebFluxFinishWiretap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.server.ServerWebExchange;

/**
 * 切面 获取webflux 请求响应body
 *
 * @author hui.zhang
 * @date 2022年09月01日 20:15
 */
@Aspect
public class WebFluxAop {


    @Pointcut("execution(public * org.springframework.web.reactive.DispatcherHandler.handle(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        WebFluxFinishWiretap wiretap = ContextUtils.getBean(WebFluxFinishWiretap.class);
        ServerWebExchange exchange = (ServerWebExchange) args[0];
        wiretap.initContext(exchange);
        ServerWebExchange newExchange = exchange.mutate()
                .request(new WebFluxRequest(exchange))
                .response(new WebFluxResponse(exchange)).build();
        args[0] = newExchange;
        Object ret = joinPoint.proceed(args);
        return wiretap.error(newExchange, ret);
    }


}
