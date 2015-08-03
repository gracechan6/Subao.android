package com.jinwang.subao.normal.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dreamy on 2015/7/2.
 */
public class ProviderSettings {

    public static interface BaseChatListColumns extends BaseColumns {
        static final String USERTEL = "user";
        static final String NAME = "name";
        static final String CONTENT = "content";
        static final String DATE = "date";
        static final String COUNT = "count";
    }

    public static interface BaseChatMessageColumns extends BaseColumns {
        static final String SENDER = "sender";
//        static final String SENDTO = "to";
        static final String CONTENT = "content";
        static final String DATE = "date";
        static final String TYPE = "type";
    }

    public static final class ChatListColumns implements BaseChatListColumns {
        /**
         * The content:// style URL for this table
         *  //Uri.parse("content://hb.android.contentProvider/teacher")
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" +
                ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_LIST /*+
                "?" + ModuleConfig.PARAMETER_NOTIFY + "=true"*/);
    }

    /**
     * Favorites.
     */
    public static final class ChatMessageColumns implements BaseChatMessageColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" +
                ModuleConfig.AUTHORITY + "/" + ModuleConfig.TABLE_CHAT_MESSAGE /*+
                "?" + ModuleConfig.PARAMETER_NOTIFY + "=true"*/);
    }
}
