/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.canal.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.scaffold.canal.context.CanalContext;
import com.scaffold.canal.model.CanalModel;
import com.scaffold.canal.util.HandlerUtil;

import java.util.List;
import java.util.Map;

/**
 * @author hui.zhang
 * @version $Id: AbstractMessageHandler.java, v 0.1 2022-04-27 下午4:38 hui.zhang Exp $$
 */
public abstract class AbstractMessageHandler implements MessageHandler<Message> {

    private Map<String, EntryHandler> tableHandlerMap;

    private RowDataHandler<CanalEntry.RowData> rowDataHandler;

    public  AbstractMessageHandler(List<? extends EntryHandler> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler) {
        this.tableHandlerMap = HandlerUtil.getTableHandlerMap(entryHandlers);
        this.rowDataHandler = rowDataHandler;
    }


    @Override
    public  void handleMessage(Message message) {
        List<CanalEntry.Entry> entries = message.getEntries();
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)) {
                try {
                    EntryHandler<?> entryHandler = HandlerUtil.getEntryHandler(tableHandlerMap, entry.getHeader().getTableName());
                    if(entryHandler!=null){
                        CanalModel model = CanalModel.builder().id(message.getId()).table(entry.getHeader().getTableName())
                                .executeTime(entry.getHeader().getExecuteTime()).database(entry.getHeader().getSchemaName()).build();
                        CanalContext.setModel(model);
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        for (CanalEntry.RowData rowData : rowDataList) {
                            rowDataHandler.handlerRowData(rowData,entryHandler,eventType);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }finally {
                    CanalContext.removeModel();
                }

            }
        }
    }

}

