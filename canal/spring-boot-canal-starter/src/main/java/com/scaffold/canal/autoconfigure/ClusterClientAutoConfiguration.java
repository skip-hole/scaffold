package com.scaffold.canal.autoconfigure;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.scaffold.canal.client.ClusterCanalClient;
import com.scaffold.canal.factory.EntryColumnModelFactory;
import com.scaffold.canal.handler.EntryHandler;
import com.scaffold.canal.handler.MessageHandler;
import com.scaffold.canal.handler.RowDataHandler;
import com.scaffold.canal.handler.impl.AsyncMessageHandlerImpl;
import com.scaffold.canal.handler.impl.RowDataHandlerImpl;
import com.scaffold.canal.handler.impl.SyncMessageHandlerImpl;
import com.scaffold.canal.properties.CanalProperties;
import com.scaffold.canal.properties.CanalSimpleProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableConfigurationProperties(CanalSimpleProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "cluster")
@Import(ThreadPoolAutoConfiguration.class)
public class ClusterClientAutoConfiguration {


    private final CanalSimpleProperties canalSimpleProperties;
    private final List<EntryHandler> entryHandlers;


    public ClusterClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties, List<EntryHandler> entryHandlers) {
        this.canalSimpleProperties = canalSimpleProperties;
        this.entryHandlers = entryHandlers;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler,
                                         ExecutorService executorService) {
        return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }


    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    @ConditionalOnMissingBean
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler) {
        return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public ClusterCanalClient clusterCanalClient(MessageHandler messageHandler) {
        return ClusterCanalClient.builder().
                canalServers(canalSimpleProperties.getServer())
                .destination(canalSimpleProperties.getDestination())
                .userName(canalSimpleProperties.getUserName())
                .messageHandler(messageHandler)
                .password(canalSimpleProperties.getPassword())
                .batchSize(canalSimpleProperties.getBatchSize())
                .filter(canalSimpleProperties.getFilter())
                .timeout(canalSimpleProperties.getTimeout())
                .unit(canalSimpleProperties.getUnit()).build();
    }
}
