package com.scaffold.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.scaffold.canal.handler.MessageHandler;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
@Builder
public class ClusterCanalClient extends AbstractCanalClient {

    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private String canalServers;
    private String destination;
    private String userName;
    private String password;
    private MessageHandler messageHandler;

    @Override
    protected CanalConnector getConnector() {
        List<InetSocketAddress> list = Stream.of(canalServers.split(",")).map(s -> {
            String[] split = s.split(":");
            return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        }).collect(Collectors.toList());
        CanalConnector connector = CanalConnectors.newClusterConnector(list, destination, userName, password);
        return connector;
    }
}
