package com.yxw.cn.carpenterrepair.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yxw.cn.carpenterrepair.activity.main.MainActivity;
import com.yxw.cn.carpenterrepair.activity.order.OrderDetailActivity;
import com.yxw.cn.carpenterrepair.activity.user.LoginActivity;
import com.yxw.cn.carpenterrepair.activity.user.PersonInfoActivity;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.JsonHelper;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			PreferencesHelper.getInstance().putString(SpConstant.REGISTER_ID, regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			receiveNotification(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			openNotification(context,bundle);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	private void receiveNotification(Context context, Bundle bundle){
		String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (Helper.isNotEmpty(extra)){
			PushExtras pushExtras = JsonHelper.fromJson(extra,PushExtras.class);
			if (pushExtras!=null){
			}
		}
	}


	private void openNotification(Context context, Bundle bundle){
		try {
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.d(TAG,extra);
			PushExtras pushExtras = JsonHelper.fromJson(extra,PushExtras.class);
			Intent intent;
			if (pushExtras!=null){
				if(CurrentUser.getInstance().isLogin()){
					String orderId = pushExtras.getId();
					if ("4".equals(pushExtras.getMessageType())){
						//订单池列表-全部
						intent = new Intent(context, MainActivity.class);
					}else if ("6".equals(pushExtras.getMessageType())){
						//用户信息页
						intent = new Intent(context, PersonInfoActivity.class);
					}else{
						//订单详情
						OrderItem orderItem = new OrderItem();
						orderItem.setOrderStatus(Integer.parseInt(pushExtras.getOrderStatus()));
						OrderItem newOrder = AppUtil.setDetailId(orderItem,orderId);
						intent = new Intent(context, OrderDetailActivity.class);
						intent.putExtra("data",newOrder);
					}
				}else{
					intent = new Intent(context,LoginActivity.class);
				}
			}else{
				intent = new Intent(context,CurrentUser.getInstance().isLogin()?MainActivity.class:LoginActivity.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}
