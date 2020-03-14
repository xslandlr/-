package com.xsl.ndhandler;

import java.util.List;

public class MyLooper {

    private static ThreadLocal<MyLooper> threadLocal = new ThreadLocal<>();
    public static  MyMessageQueue messageQueue;

    private MyLooper(){
        messageQueue = new MyMessageQueue();
    }

    public MyMessageQueue getMessageQueue(){
        return messageQueue;
    }

    /**
     * 获取当前线程的looper
     */
    public static MyLooper myLooper(){
        MyLooper myLooper = threadLocal.get();
        return myLooper;
    }

    // 给当前线程创建一个相关联的Looper对象，并存在ThreadLocal当中
    public static void prepare(){
        if(threadLocal.get() != null){
            throw new RuntimeException("Current Thread is created looper");
        }else{
            threadLocal.set(new MyLooper());
        }
    }

    public static void loop(){
        MyLooper myLooper = threadLocal.get();
        while(true){
            List<MyMessage> msgList = myLooper.messageQueue.getMsgList();
            for(int i = 0; i < msgList.size(); i++){
                MyMessage myMessage = msgList.get(i);
                myMessage.target.dispatchMessage(myMessage);
                msgList.remove(msgList.get(i));
            }
        }
    }
}
