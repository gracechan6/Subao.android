package com.jinwang.subao.normal.chat.ddpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;


public class TickAlarmReceiver extends BroadcastReceiver {

	WakeLock wakeLock;
	
	public TickAlarmReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startSrv = new Intent(context, ChatService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}

}
