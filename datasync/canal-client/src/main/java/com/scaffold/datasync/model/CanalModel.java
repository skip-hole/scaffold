package com.scaffold.datasync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * canal
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanalModel {

    /**
     * 消息id
     */
    private long id;

    /**
     * 库名
     */
    private String database;

    /**
     * 表名
     */
    private String table;

    /**
     * binlog executeTime
     */
    private Long executeTime;

    /**
     * dml build timeStamp
     */
    private Long createTime;



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CanalModel{");
        sb.append("id=").append(id);
        sb.append(", database='").append(database).append('\'');
        sb.append(", table='").append(table).append('\'');
        sb.append(", executeTime=").append(executeTime);
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
