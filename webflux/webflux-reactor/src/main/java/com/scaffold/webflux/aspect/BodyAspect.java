package com.scaffold.webflux.aspect;

import com.scaffold.webflux.support.WrapperServerRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author hui.zhang
 * @date 2022年08月28日 10:30
 */
@Aspect
@Component
public class BodyAspect {

    @Pointcut("execution(public * org.springframework.web.reactive.DispatcherHandler.handle(..))")
    public void pointcut(){

    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ServerWebExchange exchange = (ServerWebExchange) args[0];
        args[0] = exchange.mutate().request(new WrapperServerRequest(exchange)).build();
        return joinPoint.proceed(args);

    }
}
