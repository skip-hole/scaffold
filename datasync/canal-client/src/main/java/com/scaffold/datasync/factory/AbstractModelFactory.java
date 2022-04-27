package com.scaffold.datasync.factory;


import com.scaffold.datasync.handler.EntryHandler;
import com.scaffold.datasync.util.GenericUtil;
import com.scaffold.datasync.util.HandlerUtil;

public abstract class AbstractModelFactory<T> implements IModelFactory<T> {


    @Override
    public <R> R newInstance(EntryHandler entryHandler, T t) throws Exception {
        String canalTableName = HandlerUtil.getCanalTableName(entryHandler);
        if ("all".equals(canalTableName)) {
            return (R) t;
        }
        Class<R> tableClass = GenericUtil.getTableClass(entryHandler);
        if (tableClass != null) {
            return newInstance(tableClass, t);
        }
        return null;
    }


    abstract <R> R newInstance(Class<R> c, T t) throws Exception;
}
