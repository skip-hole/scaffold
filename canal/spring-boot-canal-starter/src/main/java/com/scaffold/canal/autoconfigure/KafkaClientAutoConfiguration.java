package com.scaffold.canal.autoconfigure;


import com.scaffold.canal.client.KafkaCanalClient;
import com.scaffold.canal.factory.MapColumnModelFactory;
import com.scaffold.canal.handler.EntryHandler;
import com.scaffold.canal.handler.MessageHandler;
import com.scaffold.canal.handler.RowDataHandler;
import com.scaffold.canal.handler.impl.AsyncFlatMessageHandlerImpl;
import com.scaffold.canal.handler.impl.MapRowDataHandlerImpl;
import com.scaffold.canal.handler.impl.SyncFlatMessageHandlerImpl;
import com.scaffold.canal.properties.CanalKafkaProperties;
import com.scaffold.canal.properties.CanalProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableConfigurationProperties(CanalKafkaProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "kafka")
@Import(ThreadPoolAutoConfiguration.class)
public class KafkaClientAutoConfiguration {


    private final CanalKafkaProperties canalKafkaProperties;
    private final List<EntryHandler> entryHandlers;


    public KafkaClientAutoConfiguration(CanalKafkaProperties canalKafkaProperties, List<EntryHandler> entryHandlers) {
        this.canalKafkaProperties = canalKafkaProperties;
        this.entryHandlers = entryHandlers;
    }


    @Bean
    public RowDataHandler<List<Map<String, String>>> rowDataHandler() {
        return new MapRowDataHandlerImpl(new MapColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler,
                                         ExecutorService executorService) {
        return new AsyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }


    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    @ConditionalOnMissingBean
    public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        return new SyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler);
    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaCanalClient kafkaCanalClient(MessageHandler messageHandler) {
        return KafkaCanalClient.builder().servers(canalKafkaProperties.getServer())
                .groupId(canalKafkaProperties.getGroupId())
                .topic(canalKafkaProperties.getDestination())
                .messageHandler(messageHandler)
                .batchSize(canalKafkaProperties.getBatchSize())
                .filter(canalKafkaProperties.getFilter())
                .timeout(canalKafkaProperties.getTimeout())
                .unit(canalKafkaProperties.getUnit())
                .build();
    }

}
