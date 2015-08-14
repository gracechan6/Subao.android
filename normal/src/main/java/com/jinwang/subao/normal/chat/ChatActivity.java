package com.jinwang.subao.normal.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.chat.adapter.ChatMessageViewAdapter;
import com.jinwang.subao.normal.chat.ddpush.ChatParams;
import com.jinwang.subao.normal.chat.ddpush.ChatService;
import com.jinwang.subao.normal.chat.entity.ChatMessage;
import com.jinwang.subao.normal.config.ActionConfig;
import com.jinwang.subao.normal.config.AppParams;
import com.jinwang.subao.normal.utils.ChatModel;
import com.jinwang.subao.normal.utils.PreferenceUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.ddpush.im.util.StringUtil;
import org.ddpush.im.v1.client.appserver.Pusher;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dreamy on 2015/6/25.
 */
public class ChatActivity extends BaseActivity {
    //设置标题
    private Toolbar mToolBar;
    private TextView mTitle;
    // 发送btn
    private Button mBtnSend;
    // 返回btn
//    private Button mBtnBack;
    //发送内容
    private EditText mEditTextContent;
    //视图语句显示
    private ListView mListView;
    // 消息视图的Adapter
    private ChatMessageViewAdapter mAdapter;
//    private List<ChatMessage> mRecvList = new ArrayList<ChatMessage>();// 消息对象数组
//    private List<ChatMessage> mSendList = new ArrayList<ChatMessage>();// 消息对象数组
    private List<ChatMessage> mChatList = new ArrayList<ChatMessage>();// 消息对象数组
    private String sender;
    private String userName;
    private final static int COUNT = 12;// 初始化数组总数

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        sender = bundle.getString("Sender");
        Log.i(getClass().getSimpleName(),"传过来的sender "+sender);
        AppParams.ChatObject = sender;
        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        userName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
        //定义页面头部
        initToolBar();
        initView();// 初始化view
        initData(sender);// 初始化数据
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    private void initView(){
        mListView = (ListView) findViewById(R.id.listview);
        //绑定发送按钮
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    /**
     * 模拟加载消息历史，实际开发可以从数据库中读出
     */
    public void initData(String sender) {
        mChatList = ChatModel.querySenderChatMessage(this, sender);
        ChatModel.resetUnreadCount(this, sender);
//        mSendList = ChatModel.querySenderChatMessage(this, userName, sender, mChatList);
        mAdapter = new ChatMessageViewAdapter(this, mChatList);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMessage entity = new ChatMessage();
            entity.setDate(getDate());//发出日期
            entity.setContent(contString);//发出内容
            entity.setType(0);//自己发出的消息为0
            entity.setSender(sender);//发出手机号码
//            entity.setSendTo(sender);
            mChatList.add(entity);//添加到消息列表

            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
            mEditTextContent.setText("");// 清空编辑框数据

            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
            //调用接口推送消息到服务器
            sendMessage(contString);
//            sendByDDPush(contString);根据ddpush推送消息 已停用
            //插入数据库
            ChatModel.insertChatMessageItem(this, entity);
        }
    }

    /**
     * 发送消息到服务器
     * @param message  消息内容
     */
    public void sendMessage(String message){
        SharedPreferences sp=getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_APPEND);
        final String mPhone=sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
        final String ToPhone="18815288493";
        JSONObject jb=new JSONObject();
        try {
            jb.put("from",mPhone);
            jb.put("to",ToPhone);
            jb.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url= UrlParam.SENDMESSAGE_URL;
        RequestParams params=new RequestParams();
        params.put("jsonString",jb.toString());
        AsyncHttpClient client=new AsyncHttpClient();
        Log.i(getClass().getSimpleName(),"发送连接"+url+params.toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String msg=new String(responseBody,"GBK");
                    Log.i(getClass().getSimpleName(),"得到的消息"+msg);
                    try {
                        JSONObject jo=new JSONObject(msg);
                        if(jo.getBoolean("success")){
                            Log.i(getClass().getSimpleName(),"发送消息成功");
                        }else{
                            Toast.makeText(getApplicationContext(), "发送消息失败，请重新发送", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void sendByDDPush(String sendData){
        String serverIp = ChatParams.SERVER_IP;
        String pushPort = ChatParams.PUSH_PORT;
        int port;
        try{
            port = Integer.parseInt(pushPort);
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "推送端口格式错误："+pushPort, Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] uuid = null;
        try{
            uuid = StringUtil.md5Byte(sender);
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject object = new JSONObject();
        String jsonResult = null;
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put( "from", userName );
            jsonObj.put( "to", sender );
            jsonObj.put( "message", sendData );

            jsonResult = jsonObj.toString();//生成返回字符串
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] msg = null;
        try{
            msg = jsonResult.getBytes("UTF-8");
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Thread t = new Thread(new DDPushSendTask(this,serverIp,port,uuid,msg));
        t.start();
    }

    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
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
        mTitle.setText(sender);
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.finish();
            }
        });
    }

    class DDPushSendTask implements Runnable{
        private Context context;
        private String serverIp;
        private int port;
        private byte[] uuid;
        private byte[] msg;

        public DDPushSendTask(Context context, String serverIp, int port, byte[] uuid, byte[] msg){
            this.context = context;
            this.serverIp = serverIp;
            this.port = port;
            this.uuid = uuid;
            this.msg = msg;
        }

        public void run(){
            Pusher pusher = null;
            Intent startSrv = new Intent(context, ChatService.class);
            startSrv.putExtra("CMD", "TOAST");
            try{
                boolean result;
                pusher = new Pusher(serverIp,port, 1000*5);
                result = pusher.push0x20Message(uuid,msg);
                if(result){
                    startSrv.putExtra("TEXT", "自定义信息发送成功");
                }else{
                    startSrv.putExtra("TEXT", "发送失败！格式有误");
                }
            }catch(Exception e){
                e.printStackTrace();
                startSrv.putExtra("TEXT", "发送失败！"+e.getMessage());
            }finally{
                if(pusher != null){
                    try{pusher.close();}catch(Exception e){};
                }
            }
            context.startService(startSrv);
        }
    }

    private IntentFilter mFilter = new IntentFilter();

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppParams.ChatObject = null;
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mFilter.addAction(ActionConfig.NEW_MESSAGE_RECV);
        registerReceiver(mReceiver, mFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            String action = intent.getAction();
            Log.i(getClass().getName(), action);
            //登出
            if (action.equals(ActionConfig.NEW_MESSAGE_RECV)) {
                String sendFrom = intent.getStringExtra("Sender");
                List<ChatMessage> mList = ChatModel.querySenderChatMessage(ChatActivity.this, sendFrom);
                mChatList.clear();
                mChatList.addAll(mList);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mListView.getCount() - 1);
                ChatModel.resetUnreadCount(ChatActivity.this, sender);
            }
        }
    };
}
