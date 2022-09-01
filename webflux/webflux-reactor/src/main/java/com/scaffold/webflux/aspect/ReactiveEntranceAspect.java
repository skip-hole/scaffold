package com.scaffold.webflux.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author hui.zhang
 * @date 2022年08月28日 10:30
 */
@Aspect
@Component
public class ReactiveEntranceAspect {


    @Pointcut("execution(public * com.scaffold.webflux.util.MessageUtils.subscribeWebFluxMessage(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        System.out.println("WebFlux报文数据：" + JSON.toJSONString(args[0]));
        return joinPoint.proceed(args);

    }


}
