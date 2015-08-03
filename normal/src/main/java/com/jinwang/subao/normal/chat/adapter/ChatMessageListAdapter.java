package com.jinwang.subao.normal.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.chat.entity.ChatList;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dreamy on 2015/7/1.
 */
public class ChatMessageListAdapter extends BaseAdapter {
    protected List<ChatList> mList;
//    private CircleImageView headImage;
    //Context
    protected Context mContext;
    //Layout inflater
    private LayoutInflater mInflater;

    public ChatMessageListAdapter( Context context, List<ChatList> list )
    {
        mInflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if( null != mList )
        {
            return mList.size();
        }
        return 0;
    }

    @Override
    public ChatList getItem(int position) {
        return mList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( null == convertView )
        {
            convertView = mInflater.inflate( R.layout.layout_message_item, null );
        }
        ChatList msgEntity = this.getItem(position);
//        CircleImageView headImage = (CircleImageView)convertView.findViewById(R.id.headPhoto);
//        headImage.setImageBitmap(msgEntity.);

        TextView tv = ( TextView ) convertView.findViewById( R.id.useName );
        if(msgEntity.getSenderName() == null){
            tv.setText(msgEntity.getSenderTel());
        }else {
            tv.setText(msgEntity.getSenderName());
        }
        //日期
        tv = null;
        tv = ( TextView ) convertView.findViewById( R.id.date );
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.CHINA );
        tv.setText(msgEntity.getDate());

        //内容
        tv = null;
        tv = ( TextView ) convertView.findViewById( R.id.content );
        tv.setText(msgEntity.getContent());

        return convertView;
    }
}
