# Canal 事件格式

Event {

    Header{
        logfileName     [binlog文件名]
        logfileoffset   [binlog position]
        executeTime     [binlog里记录变更发生的时间戳]
        schemaName      [数据库实例]
        tableName       [表名]
        eventType       [insert/update/delete]
    }
    entryType           [事务头BEGIN/事务尾END/数据ROWDATA]
    storeValue          [byte数据，对应的类型是RowChange]
    isDdl               [是否是ddl变更操作，比如 create table/drop table]
    sql                 [具体的ddl sql]
    rowDatas            [具体insert/update/delete 可为多条 批处理等]
    beforeColumns       [Column类型的数组]
    afterColumns        [Column类型数组]
    
}  
Column{

    index                [column序号]
    sqlType              [jdbc type]
    name                 [column name]
    isKey                [是否逐渐]
    updated              [是否发生过变更]
    isNull               [值是否为null]
    value                [具体的值]
    
}     
   