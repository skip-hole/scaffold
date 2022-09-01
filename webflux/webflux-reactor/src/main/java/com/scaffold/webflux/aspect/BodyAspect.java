package com.scaffold.webflux.aspect;

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
public class BodyAspect {


    @Pointcut("execution(public * com.scaffold.webflux.util.MessageUtils.pushMessage(..))")
    public void pointcut1() {

    }

    @Around("pointcut1()")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        return joinPoint.proceed(args);

    }


}
