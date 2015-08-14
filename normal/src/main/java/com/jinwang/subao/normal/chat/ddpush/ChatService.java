package com.jinwang.subao.normal.chat.ddpush;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;
import com.jinwang.subao.normal.MainActivity;
import com.jinwang.subao.normal.utils.ChatModel;
import com.jinwang.subao.normal.utils.PreferenceUtils;

import org.ddpush.im.util.DateTimeUtil;
import org.ddpush.im.util.StringUtil;
import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.TCPClientBase;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by dreamy on 2015/7/7.
 */
public class ChatService extends Service {
    protected PendingIntent tickPendIntent;
    protected TickAlarmReceiver tickAlarmReceiver = new TickAlarmReceiver();
    PowerManager.WakeLock wakeLock;
    MyTcpClient myTcpClient;
    Notification n;


    public class MyTcpClient extends TCPClientBase {
        public MyTcpClient(byte[] uuid, int appid, String serverAddr, int serverPort)
                throws Exception {
            super(uuid, appid, serverAddr, serverPort, 10);
        }

        @Override
        public boolean hasNetworkConnection() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        }

        @Override
        public void trySystemSleep() {
            tryReleaseWakeLock();
        }

        @Override
        public void onPushMessage(Message message) {
            if(message == null){
                return;
            }
            if(message.getData() == null || message.getData().length == 0){
                return;
            }
            if(message.getCmd() == 16){// 0x10 通用推送信息
                notifyUser(16,"DDPush通用推送信息","时间："+ DateTimeUtil.getCurDateTime(),"收到通用推送信息");
            }
            if(message.getCmd() == 17){// 0x11 分组推送信息
                long msg = ByteBuffer.wrap(message.getData(), 5, 8).getLong();
                notifyUser(17,"DDPush分组推送信息",""+msg,"收到通用推送信息");
            }
            if(message.getCmd() == 32){// 0x20 自定义推送信息
                String str = null;
                try{
                    str = new String(message.getData(),5,message.getContentLength(), "UTF-8");
                    Log.i(getClass().getSimpleName(),"得到推送的数据"+str);
                }catch(Exception e){
                    e.printStackTrace();
                }
                notifyUser(32,"DDPush自定义推送信息",""+str,"收到自定义推送信息");
            }
            setPkgsInfo();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.setTickAlarm();

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "OnlineService");

        resetClient();

//        notifyRunning();
    }

    @Override
    public int onStartCommand(Intent param, int flags, int startId) {
        if(param == null){
            return START_STICKY;
        }
        String cmd = param.getStringExtra("CMD");
        if(cmd == null){
            cmd = "";
        }
        if(cmd.equals("TICK")){
            if(wakeLock != null && wakeLock.isHeld() == false){
                wakeLock.acquire();
            }
        }
        if(cmd.equals("RESET")){
            if(wakeLock != null && wakeLock.isHeld() == false){
                wakeLock.acquire();
            }
            resetClient();
        }
        if(cmd.equals("TOAST")){
            String text = param.getStringExtra("TEXT");
            if(text != null && text.trim().length() != 0){
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }

        setPkgsInfo();

        return START_STICKY;
    }

    protected void setPkgsInfo(){
        if(this.myTcpClient == null){
            return;
        }
        long sent = myTcpClient.getSentPackets();
        long received = myTcpClient.getReceivedPackets();
        SharedPreferences account = this.getSharedPreferences(ChatParams.DEFAULT_PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.putString(ChatParams.SENT_PKGS, ""+sent);
        editor.putString(ChatParams.RECEIVE_PKGS, ""+received);
        editor.commit();
    }

    protected void resetClient(){
        String serverIp = ChatParams.SERVER_IP;
        String serverPort = ChatParams.SERVER_PORT;
        String pushPort = ChatParams.PUSH_PORT;
        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        String userName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");

        if(serverIp == null || serverIp.trim().length() == 0
                || serverPort == null || serverPort.trim().length() == 0
                || pushPort == null || pushPort.trim().length() == 0
                || userName == null || userName.trim().length() == 0){
            return;
        }
        if(this.myTcpClient != null){
            try{myTcpClient.stop();}catch(Exception e){}
        }
        try{
            myTcpClient = new MyTcpClient(StringUtil.md5Byte(userName), 1, serverIp, Integer.parseInt(serverPort));
            myTcpClient.setHeartbeatInterval(50);
            myTcpClient.start();
//            SharedPreferences.Editor editor = account.edit();
//            editor.putString(ChatParams.SENT_PKGS, "0");
//            editor.putString(ChatParams.RECEIVE_PKGS, "0");
//            editor.commit();
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "操作失败："+e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(this.getApplicationContext(), "ddpush：终端重置", Toast.LENGTH_LONG).show();
    }

    protected void tryReleaseWakeLock(){
        if(wakeLock != null && wakeLock.isHeld() == true){
            wakeLock.release();
        }
    }

    protected void setTickAlarm(){
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,TickAlarmReceiver.class);
        int requestCode = 0;
        tickPendIntent = PendingIntent.getBroadcast(this,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //小米2s的MIUI操作系统，目前最短广播间隔为5分钟，少于5分钟的alarm会等到5分钟再触发！2014-04-28
        long triggerAtTime = System.currentTimeMillis();
        int interval = 300 * 1000;
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, tickPendIntent);
    }

    protected void cancelTickAlarm(){
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(tickPendIntent);
    }

    protected void notifyRunning(){
        NotificationManager notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        n = new Notification();
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_ONE_SHOT);
        n.contentIntent = pi;
        n.setLatestEventInfo(this, "", "", pi);
        //n.defaults = Notification.DEFAULT_ALL;
        //n.flags |= Notification.FLAG_SHOW_LIGHTS;
        //n.flags |= Notification.FLAG_AUTO_CANCEL;
        n.flags |= Notification.FLAG_ONGOING_EVENT;
        n.flags |= Notification.FLAG_NO_CLEAR;
        //n.iconLevel = 5;

        n.icon = R.drawable.ic_launcher;
        n.when = System.currentTimeMillis();
//        n.tickerText = "DDPushDemoTCP正在运行";
        notificationManager.notify(0, n);
    }

    protected void cancelNotifyRunning(){
        NotificationManager notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    public void notifyUser(int id, String title, String message, String tickerText){
        PushEntity entity = null;
        try {
            entity = AbsJSONUtils.defaultInstance().JSON2Object(message, PushEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = entity.getContent();
        ChatModel.saveMessage2ChatList(this, entity);
        ChatModel.saveMessage2ChatMessage(this, entity);
//        if(BaseActivityManager.getActivityManager().currentActivity() instanceof ChatActivity && AppParams.ChatObject.endsWith(entity.getFrom())) {
//            Intent intent = new Intent();
//            intent.setAction(ActionConfig.NEW_MESSAGE_RECV);
//            intent.putExtra("Sender", entity.getFrom());
//            sendBroadcast(intent);
//        }else{
//            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification n = new Notification();
//            Intent intent = new Intent(this, ChatActivity.class);
//            intent.putExtra("Sender", entity.getFrom());
//            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            n.contentIntent = pi;
//
//            n.setLatestEventInfo(this, title, content, pi);
//            n.defaults = Notification.DEFAULT_ALL;
//            n.flags |= Notification.FLAG_SHOW_LIGHTS;
//            n.flags |= Notification.FLAG_AUTO_CANCEL;
//
//            n.icon = R.drawable.ic_launcher;
//            n.when = System.currentTimeMillis();
//            n.tickerText = tickerText;
//            notificationManager.notify(id, n);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.cancelTickAlarm();
//        cancelNotifyRunning();
        this.tryReleaseWakeLock();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
