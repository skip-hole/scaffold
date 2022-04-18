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
import com.scaffold.dynamic.datasource.processor.DsProcessor;
import com.scaffold.dynamic.datasource.support.DataSourceClassResolver;
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
 * @author hui.zhang
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * The identification of SPEL.
     */
    private static final String DYNAMIC_PREFIX = "#";

    private final DataSourceClassResolver dataSourceClassResolver;
    private final DsProcessor dsProcessor;

    public DynamicDataSourceAnnotationInterceptor(Boolean allowedPublicOnly, DsProcessor dsProcessor) {
        dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
        this.dsProcessor = dsProcessor;
    }

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
        String key = dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis());
        return key.startsWith(DYNAMIC_PREFIX) ? dsProcessor.determineDatasource(invocation, key) : key;
    }
}
