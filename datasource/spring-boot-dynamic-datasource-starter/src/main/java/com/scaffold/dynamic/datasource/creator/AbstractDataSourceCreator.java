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
package com.scaffold.dynamic.datasource.creator;

import com.scaffold.dynamic.datasource.ds.ItemDataSource;
import com.scaffold.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.scaffold.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 抽象连接池创建器
 * <p>
 * 这里主要处理一些公共逻辑，如脚本和事件等
 */
@Slf4j
public abstract class AbstractDataSourceCreator implements DataSourceCreator {

    @Autowired
    protected DynamicDataSourceProperties properties;

    /**
     * 子类去实际创建连接池
     *
     * @param dataSourceProperty 数据源信息
     * @return 实际连接池
     */
    public abstract DataSource doCreateDataSource(DataSourceProperty dataSourceProperty);

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        String publicKey = dataSourceProperty.getPublicKey();
        if (StringUtils.isEmpty(publicKey)) {
            publicKey = properties.getPublicKey();
            dataSourceProperty.setPublicKey(publicKey);
        }
        Boolean lazy = dataSourceProperty.getLazy();
        if (lazy == null) {
            lazy = properties.getLazy();
            dataSourceProperty.setLazy(lazy);
        }
        DataSource dataSource = doCreateDataSource(dataSourceProperty);
        return new ItemDataSource(dataSourceProperty.getPoolName(), dataSource, dataSource);
    }

}
