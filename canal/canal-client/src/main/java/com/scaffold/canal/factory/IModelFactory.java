package com.scaffold.canal.factory;



import com.scaffold.canal.handler.EntryHandler;

import java.util.Set;

public interface IModelFactory<T> {


    <R> R newInstance(EntryHandler entryHandler, T t) throws Exception;


    default <R> R newInstance(EntryHandler entryHandler, T t, Set<String> updateColumn) throws Exception {
        return null;
    }
}
