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
package com.scaffold.dynamic.datasource.spring.boot.autoconfigure;

import com.scaffold.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

@Slf4j
@Data
@Accessors(chain = true)
public class DataSourceProperty {

    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String poolName;
    /**
     * 连接池类型，如果不设置自动查找 HikariCp
     */
    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;
    /**
     * lazy init datasource
     */
    private Boolean lazy;
    /**
     * HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();
    /**
     * 解密公匙(如果未设置默认使用全局的)
     */
    private String publicKey;
}
