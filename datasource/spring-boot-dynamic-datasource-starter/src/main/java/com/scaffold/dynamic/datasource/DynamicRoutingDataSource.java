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
package com.scaffold.dynamic.datasource;

import com.scaffold.dynamic.datasource.ds.AbstractRoutingDataSource;
import com.scaffold.dynamic.datasource.ds.GroupDataSource;
import com.scaffold.dynamic.datasource.ds.ItemDataSource;
import com.scaffold.dynamic.datasource.exception.CannotFindDataSourceException;
import com.scaffold.dynamic.datasource.exception.ErrorCreateDataSourceException;
import com.scaffold.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.scaffold.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import com.scaffold.dynamic.datasource.strategy.LoadBalanceDynamicDataSourceStrategy;
import com.scaffold.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心动态数据源组件
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {

    private static final String UNDERLINE = "_";
    /**
     * 所有数据库
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    /**
     * 分组数据库
     */
    private final Map<String, GroupDataSource> groupDataSources = new ConcurrentHashMap<>();
    @Autowired
    private List<DynamicDataSourceProvider> providers;
    @Setter
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;
    @Setter
    private String primary = "master";
    @Setter
    private Boolean strict = false;

    @Override
    public DataSource determineDataSource() {
        String dsKey = DynamicDataSourceContextHolder.peek();
        return getDataSource(dsKey);
    }

    private DataSource determinePrimaryDataSource() {
        log.debug("dynamic-datasource switch to the primary datasource");
        DataSource dataSource = dataSourceMap.get(primary);
        if (dataSource != null) {
            return dataSource;
        }
        GroupDataSource groupDataSource = groupDataSources.get(primary);
        if (groupDataSource != null) {
            return groupDataSource.determineDataSource();
        }
        throw new CannotFindDataSourceException("dynamic-datasource can not find primary datasource");
    }

    /**
     * 获取所有的数据源
     *
     * @return 当前所有数据源
     */
    public Map<String, DataSource> getDataSources() {
        return dataSourceMap;
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (StringUtils.isEmpty(ds)) {
            return determinePrimaryDataSource();
        } else if (!groupDataSources.isEmpty() && groupDataSources.containsKey(ds)) {
            log.debug("dynamic-datasource switch to the datasource named [{}]", ds);
            return groupDataSources.get(ds).determineDataSource();
        } else if (dataSourceMap.containsKey(ds)) {
            log.debug("dynamic-datasource switch to the datasource named [{}]", ds);
            return dataSourceMap.get(ds);
        }
        if (strict) {
            throw new CannotFindDataSourceException("dynamic-datasource could not find a datasource named" + ds);
        }
        return determinePrimaryDataSource();
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(ds, dataSource);
        // 新数据源添加到分组
        this.addGroupDataSource(ds, dataSource);
        // 关闭老的数据源
        if (oldDataSource != null) {
            closeDataSource(ds, oldDataSource);
        }
        log.info("dynamic-datasource - add a datasource named [{}] success", ds);
    }

    /**
     * 新数据源添加到分组
     *
     * @param ds         新数据源的名字
     * @param dataSource 新数据源
     */
    private void addGroupDataSource(String ds, DataSource dataSource) {
        if (ds.contains(UNDERLINE)) {
            String group = ds.split(UNDERLINE)[0];
            GroupDataSource groupDataSource = groupDataSources.get(group);
            if (groupDataSource == null) {
                try {
                    groupDataSource = new GroupDataSource(group, strategy.getDeclaredConstructor().newInstance());
                    groupDataSources.put(group, groupDataSource);
                } catch (Exception e) {
                    throw new ErrorCreateDataSourceException("dynamic-datasource - add the datasource named " + ds + " error", e);
                }
            }
            groupDataSource.addDatasource(ds, dataSource);
        }
    }


    @Override
    public void destroy() throws Exception {
        log.info("dynamic-datasource start closing ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getKey(), item.getValue());
        }
        log.info("dynamic-datasource all closed success,bye");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加并分组数据源
        Map<String, DataSource> dataSources = new HashMap<>(16);
        for (DynamicDataSourceProvider provider : providers) {
            dataSources.putAll(provider.loadDataSources());
        }
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        // 检测默认数据源是否设置
        if (groupDataSources.containsKey(primary)) {
            log.info("dynamic-datasource initial loaded [{}] datasource,primary group datasource named [{}]", dataSources.size(), primary);
        } else if (dataSourceMap.containsKey(primary)) {
            log.info("dynamic-datasource initial loaded [{}] datasource,primary datasource named [{}]", dataSources.size(), primary);
        } else {
            log.warn("dynamic-datasource initial loaded [{}] datasource,Please add your primary datasource or check your configuration", dataSources.size());
        }
    }

    /**
     * close db
     *
     * @param ds         dsName
     * @param dataSource db
     */
    private void closeDataSource(String ds, DataSource dataSource) {
        try {
            if (dataSource instanceof ItemDataSource) {
                ((ItemDataSource) dataSource).close();
            } else {
                Method closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "close");
                if (closeMethod != null) {
                    closeMethod.invoke(dataSource);
                }
            }
        } catch (Exception e) {
            log.warn("dynamic-datasource closed datasource named [{}] failed", ds, e);
        }
    }

}