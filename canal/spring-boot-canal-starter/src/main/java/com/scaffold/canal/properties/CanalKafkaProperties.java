package com.scaffold.canal.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = CanalSimpleProperties.CANAL_PREFIX + ".kafka")
public class CanalKafkaProperties extends CanalProperties {


    private Integer partition;

    private String groupId;


    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
