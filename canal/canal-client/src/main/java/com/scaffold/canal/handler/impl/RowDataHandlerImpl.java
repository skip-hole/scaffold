package com.scaffold.canal.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.scaffold.canal.factory.IModelFactory;
import com.scaffold.canal.handler.EntryHandler;
import com.scaffold.canal.handler.RowDataHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RowDataHandlerImpl implements RowDataHandler<CanalEntry.RowData> {

    private IModelFactory<List<CanalEntry.Column>> modelFactory;

    public RowDataHandlerImpl(IModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    @Override
    public <R> void handlerRowData(CanalEntry.RowData rowData, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception {
        if (entryHandler != null) {
            switch (eventType) {
                case INSERT:
                    R object = modelFactory.newInstance(entryHandler, rowData.getAfterColumnsList());
                    entryHandler.insert(object);
                    break;
                case UPDATE:
                    Set<String> updateColumnSet = rowData.getAfterColumnsList().stream().filter(CanalEntry.Column::getUpdated)
                            .map(CanalEntry.Column::getName).collect(Collectors.toSet());
                    R before = modelFactory.newInstance(entryHandler, rowData.getBeforeColumnsList(),updateColumnSet);
                    R after = modelFactory.newInstance(entryHandler, rowData.getAfterColumnsList());
                    entryHandler.update(before, after);
                    break;
                case DELETE:
                    R o = modelFactory.newInstance(entryHandler, rowData.getBeforeColumnsList());
                    entryHandler.delete(o);
                    break;
                default:
                    break;
            }
        }
    }
}
