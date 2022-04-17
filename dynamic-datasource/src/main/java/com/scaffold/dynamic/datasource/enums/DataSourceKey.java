package com.scaffold.dynamic.datasource.enums;

/**
 * @author hui.zhang
 * @date 2022年04月17日 18:10
 */
public enum DataSourceKey {
    /**
     * Master data source key.
     */
    MASTER,
    /**
     * Slave alpha data source key.
     */
    SLAVE_ALPHA,
    /**
     * Slave beta data source key.
     */
    SLAVE_BETA,
    /**
     * Slave gamma data source key.
     */
    SLAVE_GAMMA;

    public String humpName() {
        String[] splits = this.name().split("_");
        if (splits.length <= 1) {
            return splits[0].toLowerCase();
        }
        String name = splits[1];
        return splits[0].toLowerCase() + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
