package com.scaffold.canal.handler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CanalThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("thread "+ t.getName()+" have a exception",e);
    }
}
