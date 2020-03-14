package com.xsl.ndhandler;


import java.util.ArrayList;
import java.util.List;

public class MyMessageQueue {

    private List<MyMessage> msgList = new ArrayList<MyMessage>();

    public void addMessage(MyMessage msg){
        msgList.add(msg);
    }

    public List<MyMessage> getMsgList(){
        return msgList;
    }
}
