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
package com.scaffold.dynamic.datasource.aop;

import com.scaffold.dynamic.datasource.annotation.DS;
import com.scaffold.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core Interceptor of Dynamic Datasource
 *
 * @author TaoYu
 * @since 1.2.0
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * 缓存方法对应的数据源
     */
    private final Map<Object, String> dsCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = determineDatasourceKey(invocation);
        DynamicDataSourceContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    private String determineDatasourceKey(MethodInvocation invocation) {
        return findKey(invocation.getMethod(), invocation.getThis());
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    public String findKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            return "";
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        String ds = this.dsCache.get(cacheKey);
        if (ds == null) {
            ds = findDataSourceAttribute(method);
            if (ds == null) {
                ds = "";
            }
            this.dsCache.put(cacheKey, ds);
        }
        return ds;
    }

    /**
     * 通过 AnnotatedElement 查找标记的注解, 映射为  DatasourceHolder
     *
     * @param ae AnnotatedElement
     * @return 数据源映射持有者
     */
    private String findDataSourceAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, DS.class);
        if (attributes != null) {
            return attributes.getString("value");
        }
        return null;
    }
}
