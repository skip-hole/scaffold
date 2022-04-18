/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.dynamic.datasource.spring.boot.autoconfigure;

import com.scaffold.dynamic.datasource.DynamicRoutingDataSource;
import com.scaffold.dynamic.datasource.annotation.DS;
import com.scaffold.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.scaffold.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.scaffold.dynamic.datasource.processor.DsProcessor;
import com.scaffold.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.scaffold.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.scaffold.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: DynamicDataSourceAutoConfiguration.java, v 0.1 2022-04-18 下午2:22 hui.zhang Exp $$
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@Import(value = {DynamicDataSourceCreatorAutoConfiguration.class})
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration implements InitializingBean {

    private final DynamicDataSourceProperties properties;

    private final List<DynamicDataSourcePropertiesCustomizer> dataSourcePropertiesCustomizers;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties properties,
                                              ObjectProvider<List<DynamicDataSourcePropertiesCustomizer>> dataSourcePropertiesCustomizers) {
        this.properties = properties;
        this.dataSourcePropertiesCustomizers = dataSourcePropertiesCustomizers.getIfAvailable();
    }

    @Bean
    public DynamicDataSourceProvider ymlDynamicDataSourceProvider() {
        return new YmlDynamicDataSourceProvider(properties.getDatasource());
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        return dataSource;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDatasourceAopProperties aopProperties = properties.getAop();
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(aopProperties.getAllowedPublicOnly(), dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
        advisor.setOrder(aopProperties.getOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(dataSourcePropertiesCustomizers)) {
            for (DynamicDataSourcePropertiesCustomizer customizer : dataSourcePropertiesCustomizers) {
                customizer.customize(properties);
            }
        }
    }
}

