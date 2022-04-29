package com.scaffold.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.scaffold.canal.handler.MessageHandler;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@Data
public class KafkaCanalClient extends AbstractCanalClient {


    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private String servers;
    private String topic;
    private Integer partition;
    private String groupId;
    private MessageHandler messageHandler;


    @Override
    public void process() {
        KafkaCanalConnector connector = (KafkaCanalConnector) getConnector();
        MessageHandler messageHandler = getMessageHandler();
        while (flag) {
            try {
                connector.connect();
                connector.subscribe();
                while (flag) {
                    try {
                        List<FlatMessage> messages = connector.getFlatListWithoutAck(timeout, unit);
                        log.info("获取消息 {}", messages);
                        if (messages != null) {
                            for (FlatMessage flatMessage : messages) {
                                messageHandler.handleMessage(flatMessage);
                            }
                        }
                        connector.ack();
                    } catch (Exception e) {
                        log.error("canal 消费异常", e);
                    }
                }
            } catch (Exception e) {
                log.error("canal 连接异常", e);
            }
        }
        connector.unsubscribe();
        connector.disconnect();
    }

    @Override
    protected CanalConnector getConnector() {
        return new KafkaCanalConnector(servers, topic, partition, groupId, batchSize, true);
    }
}
