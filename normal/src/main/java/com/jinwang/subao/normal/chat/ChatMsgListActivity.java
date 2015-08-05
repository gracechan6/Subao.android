package com.jinwang.subao.normal.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.chat.adapter.ChatMessageListAdapter;
import com.jinwang.subao.normal.chat.entity.ChatList;
import com.jinwang.subao.normal.utils.ChatModel;

import java.util.List;

/**
 * Created by dreamy on 2015/6/30.
 */
public class ChatMsgListActivity extends BaseActivity {
    private Toolbar mToolBar;
    private TextView mTitle;
    protected List<ChatList> mList;
    protected ChatMessageListAdapter adapter;
    private ListView mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        initToolBar();
//        ChatModel.insertChatListTestData(this);
//        mListView = (ListView) findViewById(R.id.listview);
        mListView = (ListView) findViewById(R.id.listview);
        mList = ChatModel.queryChatList(this);
        adapter = new ChatMessageListAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ChatMsgListActivity.this, ChatActivity.class);
                intent.putExtra("Sender", adapter.getItem(arg2).getSenderTel());
                startActivity(intent);

                Log.i(this.getClass().getName(), "Item clicked");
            }} );
    }

    protected void initToolBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.toolbar);

        //设置自定义标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setText(getString(R.string.action_message));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsgListActivity.this.finish();
            }
        });
    }
}
