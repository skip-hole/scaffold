package com.scaffold.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.scaffold.canal.handler.MessageHandler;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


@Data
@Builder
public class SimpleCanalClient extends AbstractCanalClient {


    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private String hostname;
    private Integer port;
    private String destination;
    private String userName;
    private String password;
    private MessageHandler messageHandler;

    @Override
    protected CanalConnector getConnector() {
        return CanalConnectors.newSingleConnector(new InetSocketAddress(hostname, port), destination, userName, password);
    }

}
