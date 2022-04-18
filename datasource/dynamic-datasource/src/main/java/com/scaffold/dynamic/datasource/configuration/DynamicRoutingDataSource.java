package com.scaffold.dynamic.datasource.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author hui.zhang
 * @date 2022年04月17日 18:34
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = DynamicDataSourceContextHolder.getDataSourceKey();
        log.info("Current DataSource is [{}]", dataSourceKey);
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
