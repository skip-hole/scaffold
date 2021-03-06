package com.scaffold.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.scaffold.canal.handler.MessageHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCanalClient implements CanalClient {


    protected volatile boolean flag;


    private Logger log = LoggerFactory.getLogger(AbstractCanalClient.class);


    private Thread workThread;


    protected String filter = StringUtils.EMPTY;


    protected Integer batchSize = 1;


    protected Long timeout = 1L;


    protected TimeUnit unit = TimeUnit.SECONDS;


    @Override
    public void start() {
        log.info("start canal client");
        workThread = new Thread(this::process);
        workThread.setName("canal-client-thread");
        flag = true;
        workThread.start();
    }

    @Override
    public void stop() {
        log.info("stop canal client");
        flag = false;
        if (null != workThread) {
            workThread.interrupt();
        }

    }

    @Override
    public void process() {
        CanalConnector connector = getConnector();
        while (flag) {
            try {
                connector.connect();
                connector.subscribe(filter);
                while (flag) {
                    Message message = connector.getWithoutAck(batchSize, timeout, unit);
                    log.info("获取消息 {}", message);
                    long batchId = message.getId();
                    if (message.getId() != -1 && message.getEntries().size() != 0) {
                        getMessageHandler().handleMessage(message);
                    }
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                log.error("canal client 异常", e);
            } finally {
                connector.disconnect();
            }
        }
    }

    protected abstract CanalConnector getConnector();


    protected abstract MessageHandler getMessageHandler();
}
