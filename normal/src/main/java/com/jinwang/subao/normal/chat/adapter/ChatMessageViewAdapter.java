package com.jinwang.subao.normal.chat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.chat.entity.ChatMessage;
import com.jinwang.subao.normal.utils.PreferenceUtils;

import java.util.List;

/**
 * Created by dreamy on 2015/6/26.
 */
public class ChatMessageViewAdapter extends BaseAdapter {
    public static interface IMViewType {
        int IM_RECV_MSG = 0;// 收到对方的消息
        int IM_SEND_MSG = 1;// 自己发送出去的消息
    }

    private static final int ITEMCOUNT = 2;// 消息类型的总数
    private List<ChatMessage> coll;// 消息对象数组
    private LayoutInflater mInflater;

    public ChatMessageViewAdapter(Context context, List<ChatMessage> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        ChatMessage entity = coll.get(position);

        if (entity.getType() == 1) {//收到的消息
            return IMViewType.IM_RECV_MSG;
        } else {//自己发送的消息
            return IMViewType.IM_SEND_MSG;
        }
    }

    /**
     * Item类型的总数
     */
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage entity = coll.get(position);
        int isComMsg = entity.getType();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg == 1) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
//            viewHolder.tvUserName = (TextView) convertView
//                    .findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSendTime.setText(entity.getDate());
//        viewHolder.tvUserName.setText(entity.getFromName());
        viewHolder.tvContent.setText(entity.getContent());
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public int isComMsg = -1;
    }

}
