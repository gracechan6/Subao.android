package com.jinwang.subao.normal.chat.entity;

/**
 * Created by dreamy on 2015/7/2.
 */
public class ChatMessage {
    private String sender;
//    private String sendTo;
    private int type;
    private String content;
    private String date;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

//    public String getSendTo() {
//        return sendTo;
//    }
//
//    public void setSendTo(String sendTo) {
//        this.sendTo = sendTo;
//    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
