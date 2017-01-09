package com.android.system.emial;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Administrator on 2016-12-18.
 */

public class MyThread extends Thread {
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    public void run() {

        try {
            RegPhone(name);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    /***
     * @param phoneSec
     */
    public void RegPhone(String phoneSec) {
        // 命名空间
        String nameSpace = "http://120.25.255.227:808/";
        // 调用的方法名称
        String methodName = "RegPhone";
        // EndPoint
        String endPoint = "http://120.25.255.227:808/WebService.asmx?wsdl";
        // SOAP Action
        String soapAction = "http://120.25.255.227:808/RegPhone";

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        rpc.addProperty("PhoneNum", phoneSec);
        //rpc.addProperty("userId", "");

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(endPoint);
        try {
            // 调用WebService
            transport.call(soapAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        try {
            String result = object.getProperty(0).toString();
            Log.i("WebService返回的结果：", result);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 将WebService返回的结果显示在TextView中

        //resultView.setText(result);

    }
}
