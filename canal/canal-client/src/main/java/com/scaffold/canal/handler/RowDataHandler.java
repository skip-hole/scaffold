/**
 * OPay Inc.
 * Copyright (c) 2016-2022 All Rights Reserved.
 */
package com.scaffold.canal.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * @author hui.zhang
 * @version $Id: RowDataHandler.java, v 0.1 2022-04-27 下午4:42 hui.zhang Exp $$
 */
public interface RowDataHandler<T> {

    <R> void handlerRowData(T t, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception;
}
