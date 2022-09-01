package com.scaffold.webflux.message;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author hui.zhang
 * @date 2022年09月01日 20:15
 */
@Aspect
public class MessageAspect {


    @Pointcut("execution(public * org.springframework.web.reactive.DispatcherHandler.handle(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ServerWebExchange exchange = (ServerWebExchange) args[0];
        args[0] = exchange.mutate()
                .request(new WrapperServerRequest(exchange))
                .response(new WrapperServerResponse(exchange)).build();
        return joinPoint.proceed(args);

    }

}
