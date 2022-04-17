package com.scaffold.dynamic.datasource.configuration;

import com.scaffold.dynamic.datasource.enums.DataSourceKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hui.zhang
 * @date 2022年04月17日 18:28
 */
@Configuration
public class DataSourceConfiguration {

    @Bean("master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    @Bean("slaveAlpha")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-alpha")
    public DataSource slaveAlpha() {
        return DataSourceBuilder.create().build();
    }

    @Bean("slaveBeta")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-beta")
    public DataSource slaveBeta() {
        return DataSourceBuilder.create().build();
    }

    @Bean("slaveGamma")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-gamma")
    public DataSource slaveGamma() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.MASTER.humpName(), master());
        dataSourceMap.put(DataSourceKey.SLAVE_ALPHA.humpName(), slaveAlpha());
        dataSourceMap.put(DataSourceKey.SLAVE_BETA.humpName(), slaveBeta());

        // Set master datasource as default
        dynamicRoutingDataSource.setDefaultTargetDataSource(master());
        // Set master and slave datasource as target datasource
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        // To put datasource keys into DataSourceContextHolder to judge if the datasource is exist
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());

        // To put slave datasource keys into DataSourceContextHolder to load balance
        DynamicDataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
        DynamicDataSourceContextHolder.slaveDataSourceKeys.remove(DataSourceKey.MASTER.humpName());
        return dynamicRoutingDataSource;
    }
}
