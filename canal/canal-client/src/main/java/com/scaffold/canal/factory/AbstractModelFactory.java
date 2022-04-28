package com.scaffold.canal.factory;


import com.scaffold.canal.handler.EntryHandler;
import com.scaffold.canal.util.GenericUtil;
import com.scaffold.canal.util.HandlerUtil;

public abstract class AbstractModelFactory<T> implements IModelFactory<T> {


    @Override
    public <R> R newInstance(EntryHandler entryHandler, T t) throws Exception {
        Class<R> tableClass = GenericUtil.getTableClass(entryHandler);
        if (tableClass != null) {
            return newInstance(tableClass, t);
        }
        return null;
    }


    abstract <R> R newInstance(Class<R> c, T t) throws Exception;
}
