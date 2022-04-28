package com.scaffold.canal.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.scaffold.canal.handler.AbstractMessageHandler;
import com.scaffold.canal.handler.EntryHandler;
import com.scaffold.canal.handler.RowDataHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class AsyncMessageHandlerImpl extends AbstractMessageHandler {


    private ExecutorService executor;


    public AsyncMessageHandlerImpl(List<? extends EntryHandler> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler, ExecutorService executor) {
        super(entryHandlers, rowDataHandler);
        this.executor = executor;
    }

    @Override
    public void handleMessage(Message message) {
        executor.execute(() -> super.handleMessage(message));
    }
}
