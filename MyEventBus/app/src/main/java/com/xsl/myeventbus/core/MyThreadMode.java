package com.xsl.myeventbus.core;

public enum MyThreadMode {

    // 谁发送的事件就在谁的线程中执行
    POSTING,
    // 在主线程中执行
    MAIN,
    // 在子线程中执行
    ASYNC

}
