package com.jinwang.subao.normal.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by dreamy on 2015/7/2.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = "DatabaseHelper";
    private long mMaxChatId = -1;
    private long mMaxMessageId = -1;

    DatabaseHelper(Context context) {
        super(context, ModuleConfig.DATABASE_NAME, null, ModuleConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating new database");
//        mMaxChatId = 0;
//        mMaxMessageId = 0;
        createChatListTable(db);
        createChatMessageTable(db);
//        if (mMaxChatId == -1) {
//            mMaxChatId = initializeMaxChatId(getWritableDatabase());
//        }
//        if(mMaxMessageId == -1){
//            mMaxMessageId = initializeMaxMessageId(getWritableDatabase());
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createChatListTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tab_chat_list(" +
                "_id INTEGER PRIMARY KEY," +
                "user TEXT," +
                "name TEXT," +
                "content TEXT," +
                "date TEXT," +
                "count INTEGER" +
                ");");
    }

    private void createChatMessageTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tab_chat_message(" +
                "_id INTEGER PRIMARY KEY," +
                "sender TEXT," +
//                "sendTo TEXT," +
                "content TEXT," +
                "type INTEGER," +
                "date TEXT" +
                ");");
    }

    public long initializeMaxChatId(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(_id) FROM tab_chat_list", null);

        // get the result
        final int maxIdIndex = 0;
        long id = -1;
        if (c != null && c.moveToNext()) {
            id = c.getLong(maxIdIndex);
        }
        if (c != null) {
            c.close();
        }

        if (id == -1) {
            throw new RuntimeException("Error: could not query max item id");
        }

        return id;
    }

    public long initializeMaxMessageId(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(_id) FROM tab_chat_message", null);

        // get the result
        final int maxIdIndex = 0;
        long id = -1;
        if (c != null && c.moveToNext()) {
            id = c.getLong(maxIdIndex);
        }
        if (c != null) {
            c.close();
        }

        if (id == -1) {
            throw new RuntimeException("Error: could not query max item id");
        }

        return id;
    }

    public long generateNewChatListId() {
        if (mMaxChatId < 0) {
            throw new RuntimeException("Error: max item id was not initialized");
        }
        mMaxChatId += 1;
        return mMaxChatId;
    }

    public long generateNewChatMessageId() {
        if (mMaxMessageId < 0) {
            throw new RuntimeException("Error: max item id was not initialized");
        }
        mMaxMessageId += 1;
        return mMaxMessageId;
    }
}
