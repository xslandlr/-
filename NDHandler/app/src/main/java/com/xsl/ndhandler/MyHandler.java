package com.xsl.ndhandler;

import android.os.Message;

public class MyHandler {

    private MyMessageQueue messageQueue;

    // 获取Looper与消息队列
    public MyHandler(){
        MyLooper myLooper = MyLooper.myLooper();
        messageQueue = myLooper.getMessageQueue();
    }

    // 分发消息
    public void dispatchMessage(MyMessage msg){
        handleMessage(msg);
    }

    // 发送消息
    public void sendMessage(MyMessage message) {
        message.target = this;
        messageQueue.addMessage(message);
    }

    public void handleMessage(MyMessage msg) { }
}
