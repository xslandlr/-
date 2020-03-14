package com.xsl.myeventbus;

public class MessageEvent {
    String str;

    public MessageEvent(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
