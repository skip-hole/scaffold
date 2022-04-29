package com.scaffold.canal.autoconfigure;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.scaffold.canal.client.SimpleCanalClient;
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
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "simple", matchIfMissing = true)
@Import(ThreadPoolAutoConfiguration.class)
public class SimpleClientAutoConfiguration {


    private final CanalSimpleProperties canalSimpleProperties;
    private final List<EntryHandler> entryHandlers;


    public SimpleClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties, List<EntryHandler> entryHandlers) {
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
    public SimpleCanalClient simpleCanalClient(MessageHandler messageHandler) {
        String server = canalSimpleProperties.getServer();
        String[] array = server.split(":");
        return SimpleCanalClient.builder()
                .hostname(array[0])
                .port(Integer.parseInt(array[1]))
                .destination(canalSimpleProperties.getDestination())
                .userName(canalSimpleProperties.getUserName())
                .password(canalSimpleProperties.getPassword())
                .messageHandler(messageHandler)
                .batchSize(canalSimpleProperties.getBatchSize())
                .filter(canalSimpleProperties.getFilter())
                .timeout(canalSimpleProperties.getTimeout())
                .unit(canalSimpleProperties.getUnit())
                .build();
    }

}
