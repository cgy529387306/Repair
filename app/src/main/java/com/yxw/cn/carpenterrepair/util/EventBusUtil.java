package com.yxw.cn.carpenterrepair.util;

import com.yxw.cn.carpenterrepair.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {

    public static void register(Object object) {
        EventBus.getDefault().register(object);
    }

    public static void unregister(Object object) {
        EventBus.getDefault().unregister(object);
    }

    public static void post(String messageId) {
        EventBus.getDefault().post(new MessageEvent(messageId));
    }

    public static void post(String messageId, Object data) {
        EventBus.getDefault().post(new MessageEvent(messageId, true, data));
    }

    public static void postFail(String messageId) {
        EventBus.getDefault().post(new MessageEvent(messageId, false));
    }

    public static void postFail(String messageId, Object data) {
        EventBus.getDefault().post(new MessageEvent(messageId, false, data));
    }
}
