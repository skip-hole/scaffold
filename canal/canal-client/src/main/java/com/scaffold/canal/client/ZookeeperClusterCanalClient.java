package com.scaffold.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.scaffold.canal.handler.MessageHandler;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Builder
public class ZookeeperClusterCanalClient extends AbstractCanalClient {


    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private String zkServers;
    private String destination;
    private String userName;
    private String password;
    private MessageHandler messageHandler;

    @Override
    protected CanalConnector getConnector() {
        return CanalConnectors.newClusterConnector(zkServers, destination, userName, password);
    }
}
