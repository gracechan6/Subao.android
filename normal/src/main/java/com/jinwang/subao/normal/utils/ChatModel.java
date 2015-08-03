package com.jinwang.subao.normal.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.jinwang.subao.normal.chat.ddpush.PushEntity;
import com.jinwang.subao.normal.chat.entity.ChatList;
import com.jinwang.subao.normal.chat.entity.ChatMessage;
import com.jinwang.subao.normal.provider.AppProvider;
import com.jinwang.subao.normal.provider.ModuleConfig;
import com.jinwang.subao.normal.provider.ProviderSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by dreamy on 2015/7/2.
 */
public class ChatModel {

    public static List<ChatList> queryChatList(Context context){
        ArrayList<ChatList> chatLists = new ArrayList<ChatList>();
        ContentResolver cr = context.getContentResolver();
        // 查找id为1的数据 //Uri.parse("content://hb.android.contentProvider/teacher")
        Uri uri = ProviderSettings.ChatListColumns.CONTENT_URI;
        Cursor c = cr.query(uri, null, null,null, null);
        if(c != null) {
            System.out.println(c.getCount());

            final int userTelIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.USERTEL);
            final int nameIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.NAME);
            final int contentIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.CONTENT);
            final int dateIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.DATE);
            final int countIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.COUNT);
            while (c.moveToNext()) {
                ChatList chatItem = new ChatList();
                chatItem.setSenderTel(c.getString(userTelIndex));
                chatItem.setSenderName(c.getString(nameIndex));
                chatItem.setContent(c.getString(contentIndex));
                chatItem.setDate(c.getString(dateIndex));
                chatItem.setCount(c.getInt(countIndex));
                chatLists.add(chatItem);
            }
        }
        c.close();
        return chatLists;
    }

    public static void insertChatMessageItem(Context context, ChatMessage msgItem){
        ContentResolver contentResolver = context.getContentResolver();
        Uri insertUri = ProviderSettings.ChatMessageColumns.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(ProviderSettings.ChatMessageColumns.SENDER, msgItem.getSender());
//        values.put(ProviderSettings.ChatMessageColumns.SENDTO, msgItem.getSendTo());
        values.put(ProviderSettings.ChatMessageColumns.CONTENT, msgItem.getContent());
        values.put(ProviderSettings.ChatMessageColumns.DATE, msgItem.getDate());
        values.put(ProviderSettings.ChatMessageColumns.TYPE, msgItem.getType());
        Uri uri = contentResolver.insert(insertUri, values);
    }

    public static List<ChatMessage> querySenderChatMessage(Context context, String sender){
        ArrayList<ChatMessage> chatLists = new ArrayList<ChatMessage>();
        ContentResolver cr = context.getContentResolver();
        String urlStr = "content://" +
                ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_MESSAGE;
        Uri uri = Uri.parse(urlStr);
        String selection = "sender=?";
        String[] selectionArgs = new String[]{sender};
        Cursor c = cr.query(uri, null, selection,selectionArgs, ProviderSettings.BaseChatMessageColumns.DATE);
        if(c != null) {
            System.out.println(c.getCount());

//            final int idIndex = c.getColumnIndexOrThrow(ProviderSettings.BaseChatListColumns._ID);
            final int senderIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatMessageColumns.SENDER);
//            final int toIndex = c.getColumnIndexOrThrow
//                    (ProviderSettings.BaseChatMessageColumns.SENDTO);
            final int contentIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatMessageColumns.CONTENT);
            final int dateIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatMessageColumns.DATE);
            final int typeIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatMessageColumns.TYPE);
            while (c.moveToNext()) {
                ChatMessage chatItem = new ChatMessage();
                chatItem.setSender(c.getString(senderIndex));
//                chatItem.setSendTo(c.getString(toIndex));
                chatItem.setContent(c.getString(contentIndex));
                chatItem.setDate(c.getString(dateIndex));
                chatItem.setType(c.getInt(typeIndex));
                chatLists.add(chatItem);
            }
        }
        c.close();
        return chatLists;
    }

    public static int querySenderInChatList(Context context, String sender){
        int retCount = -1;
        ContentResolver cr = context.getContentResolver();
        String urlStr = "content://" + ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_LIST;
        Uri uri = Uri.parse(urlStr);
        String selection = ProviderSettings.BaseChatListColumns.USERTEL + "= ?";
//        String selection = "user=?";
        String[] selectionArgs = new String[]{sender};
        Cursor c = cr.query(uri, null, selection, selectionArgs, null);
        if(c != null) {
            final int countIndex = c.getColumnIndexOrThrow
                    (ProviderSettings.BaseChatListColumns.COUNT);
            while (c.moveToNext()) {
                retCount = c.getInt(countIndex);
                c.close();
                return retCount;
            }
        }
        c.close();
        return retCount;
    }

    public static void saveMessage2ChatMessage(Context context, PushEntity entity){
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        long id = AppProvider.mOpenHelper.initializeMaxMessageId(AppProvider.mOpenHelper.getWritableDatabase()) + 1;
//        long id = AppProvider.mOpenHelper.generateNewChatListId();
        values.put(ProviderSettings.ChatMessageColumns._ID, id);
        values.put(ProviderSettings.ChatMessageColumns.SENDER, entity.getFrom());
        values.put(ProviderSettings.ChatMessageColumns.CONTENT, entity.getContent());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        values.put(ProviderSettings.ChatMessageColumns.DATE, date);
        values.put(ProviderSettings.ChatMessageColumns.TYPE, 1);
        Uri uri2 = cr.insert(ProviderSettings.ChatMessageColumns.CONTENT_URI, values);
    }

    public static void saveMessage2ChatList(Context context, PushEntity entity){
        if(entity != null) {
            int ret = querySenderInChatList(context, entity.getFrom());
            if (ret == -1) {
                insertItem2ChatList(context, entity);
            }else{
                updateSenderInChatList(context, entity);
            }
        }
    }

    private static void updateSenderInChatList(Context context, PushEntity entity){
        int count = 0;
        long chatId = -1;
        ContentResolver cr = context.getContentResolver();
        String urlStr = "content://" + ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_LIST;
        Uri uri = Uri.parse(urlStr);
        String selection = "user=?";
        String[] selectionArgs = new String[]{entity.getFrom()};
        Cursor c = cr.query(uri, null, selection, selectionArgs, null);
        if(c != null) {
            int idIndex = c.getColumnIndexOrThrow(ProviderSettings.BaseChatListColumns._ID);
            int countIndex = c.getColumnIndexOrThrow(ProviderSettings.BaseChatListColumns.COUNT);
            while (c.moveToNext()) {
                chatId = c.getInt(idIndex);
                count = c.getInt(countIndex);
            }
            ContentValues values = new ContentValues();
            values.put(ProviderSettings.ChatListColumns.CONTENT, entity.getContent());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new Date());
            values.put(ProviderSettings.ChatListColumns.DATE, date);
            values.put(ProviderSettings.ChatListColumns.COUNT, count + 1);
            String where = ProviderSettings.BaseChatListColumns._ID + "= ?";
            String[] args = {Long.toString(chatId)};
//            String[] args = new String[]{Long.toString(chatId)};
            cr.update(uri,values,where,args);
            c.close();
        }
        c.close();
    }

    public static void insertItem2ChatList(Context context, PushEntity entity){
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        long id = AppProvider.mOpenHelper.initializeMaxChatId(AppProvider.mOpenHelper.getWritableDatabase()) + 1;
//        long id = AppProvider.mOpenHelper.generateNewChatListId();
        values.put(ProviderSettings.ChatListColumns._ID, id);
        values.put(ProviderSettings.ChatListColumns.USERTEL, entity.getFrom());
        values.put(ProviderSettings.ChatListColumns.CONTENT, entity.getContent());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        values.put(ProviderSettings.ChatListColumns.DATE, date);
        values.put(ProviderSettings.ChatListColumns.COUNT, 1);
        Uri uri2 = cr.insert(ProviderSettings.ChatListColumns.CONTENT_URI, values);
    }

    public static void resetUnreadCount(Context context,String sender){
        int count = 0;
        long chatId = -1;
        ContentResolver cr = context.getContentResolver();
        String urlStr = "content://" + ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_LIST;
        Uri uri = Uri.parse(urlStr);
        String selection = "user=?";
        String[] selectionArgs = new String[]{sender};
        Cursor c = cr.query(uri, null, selection, selectionArgs, null);
        if(c != null) {
            int idIndex = c.getColumnIndexOrThrow(ProviderSettings.BaseChatListColumns._ID);
            while (c.moveToNext()) {
                chatId = c.getInt(idIndex);
            }
            ContentValues values = new ContentValues();
            values.put(ProviderSettings.ChatListColumns.COUNT, 0);
            String where = ProviderSettings.BaseChatListColumns._ID + "= ?";
            String[] args = {Long.toString(chatId)};
            cr.update(uri,values,where,args);
            c.close();
        }
        c.close();
    }

    public static void insertChatListTestData(Context context){
        ChatList chatItem = new ChatList();
        chatItem.setSenderTel("18999999999");
        chatItem.setSenderName("老大");
        chatItem.setContent("第一条测试消息");
        chatItem.setDate("2015-7-2 14:04:38");
        chatItem.setCount(1);

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ProviderSettings.ChatListColumns._ID, 1);
        values.put(ProviderSettings.ChatListColumns.USERTEL, chatItem.getSenderTel());
        values.put(ProviderSettings.ChatListColumns.NAME, chatItem.getSenderName());
        values.put(ProviderSettings.ChatListColumns.CONTENT, chatItem.getContent());
        values.put(ProviderSettings.ChatListColumns.DATE, chatItem.getDate());
        values.put(ProviderSettings.ChatListColumns.COUNT, chatItem.getCount());
        Uri uri2 = cr.insert(ProviderSettings.ChatListColumns.CONTENT_URI, values);
        System.out.println(uri2.toString());
    }


}
