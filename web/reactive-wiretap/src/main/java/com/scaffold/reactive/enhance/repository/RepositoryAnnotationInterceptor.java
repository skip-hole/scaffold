/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scaffold.reactive.enhance.repository;

import com.scaffold.reactive.util.AopClassUtils;
import com.scaffold.reactive.util.ContextUtils;
import com.scaffold.reactive.wiretap.repository.RepositoryFinishWiretap;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hui.zhang
 */
public class RepositoryAnnotationInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (!Publisher.class.isAssignableFrom(returnType)) {
            return invocation.proceed();
        }
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        Map<String, Object> params = new HashMap<>();
        params.put("requestParams", args);
        Object target = AopClassUtils.getTarget(invocation.getThis());
        params.put("className", method.getDeclaringClass().getName());
        params.put("proxyClassName", target.getClass().getName());
        params.put("methodName", method.getName());
        params.put("returnTypeName", returnType.getName());
        Object result = invocation.proceed();
        List<Object> list = new ArrayList<>();
        if (result instanceof Flux) {
            result = ((Flux) result).doOnNext(m -> {
                list.add(m);
            }).doFinally(m -> {
                ContextUtils.getBean(RepositoryFinishWiretap.class).finish(params, list);
            });
        }
        return result;
    }
}
