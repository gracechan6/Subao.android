package com.jinwang.subao.normal.chat.entity;

//import app.frame.provider.ProviderSettings;

/**
 * Created by dreamy on 2015/7/2.
 */
public class ChatList {
    private String senderTel;
    private String senderName;
    private String content;
    private String date;
    private int count;

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

//    void onAddToDatabase(ContentValues values) {
//        values.put(ProviderSettings.ChatListColumns.USERTEL, itemType);
//        values.put(ProviderSettings.ChatListColumns.CONTAINER, container);
//        values.put(ProviderSettings.ChatListColumns.SCREEN, screenId);
//        values.put(ProviderSettings.ChatListColumns.CELLX, cellX);
//        values.put(ProviderSettings.ChatListColumns.CELLY, cellY);
//        values.put(ProviderSettings.ChatListColumns.SPANX, spanX);
//        values.put(ProviderSettings.ChatListColumns.SPANY, spanY);
//    }
}
