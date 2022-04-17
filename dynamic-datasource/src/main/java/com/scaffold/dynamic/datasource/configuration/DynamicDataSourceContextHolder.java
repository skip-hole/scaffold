package com.scaffold.dynamic.datasource.configuration;

import com.scaffold.dynamic.datasource.enums.DataSourceKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hui.zhang
 * @date 2022年04月17日 18:09
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    private static AtomicInteger counter =new  AtomicInteger(0);

    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.MASTER::name);

    /**
     * All DataSource List
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * The constant slaveDataSourceKeys.
     */
    public static List<Object> slaveDataSourceKeys = new ArrayList<>();


    public static void setDataSource(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static void chooseMasterDataSource() {
        CONTEXT_HOLDER.set(DataSourceKey.MASTER.name());
    }

    public static void chooseSlaveDataSource() {
        try {
            int datasourceKeyIndex = counter.get()% slaveDataSourceKeys.size();
            CONTEXT_HOLDER.set(String.valueOf(slaveDataSourceKeys.get(datasourceKeyIndex)));
            counter.incrementAndGet();
        } catch (Exception e) {
            chooseMasterDataSource();
        }
    }

    /**
     * Get current DataSource
     *
     * @return data source key
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * To set DataSource as default
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

}


