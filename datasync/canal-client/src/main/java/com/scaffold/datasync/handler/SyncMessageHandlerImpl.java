/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.datasync.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.util.List;

/**
 * @author hui.zhang
 * @version $Id: SyncMessageHandlerImpl.java, v 0.1 2022-04-27 下午5:08 hui.zhang Exp $$
 */
public class SyncMessageHandlerImpl extends AbstractMessageHandler{
    public SyncMessageHandlerImpl(List<? extends EntryHandler> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler) {
        super(entryHandlers, rowDataHandler);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
    }
}

