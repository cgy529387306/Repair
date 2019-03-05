package com.yxw.cn.carpenterrepair.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.yxw.cn.carpenterrepair.BaseApplication;
import com.yxw.cn.carpenterrepair.contast.SpConstant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SpUtil {
    public SpUtil() {
    }

    public static <T> void saveObject(String nodeName, Object data) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(e);
            objectOutputStream.writeObject(data);
            String listBase64 = new String(Base64.encode(e.toByteArray(), 0));
            objectOutputStream.close();
            putStr(nodeName, listBase64);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static Object jsonToObject(String json) {
        try {
            byte[] e = Base64.decode(json.getBytes(), 0);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(e);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static SharedPreferences getSharedPreferences(Context context, String spName) {
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        return sp;
    }

    public static void clear() {
        Editor editor = getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).edit();
        editor.clear();
        editor.commit();
    }


    public static void putInt(String nodeName, int value) {
        Editor editor = getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).edit();
        editor.putInt(nodeName, value).commit();
    }

    public static void putBoolean(String nodeName, boolean value) {
        Editor editor = getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).edit();
        editor.putBoolean(nodeName, value).commit();
    }

    public static void putStr(String nodeName, String value) {
        Editor editor = getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).edit();
        editor.putString(nodeName, value).commit();
    }

    public static String getStr(String nodeName) {
        return getStr(nodeName, "");
    }

    public static String getStr(String nodeName, String defaultValue) {
        return getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).getString(nodeName, defaultValue);
    }

    public static boolean getBoolean(String nodeName) {
        return getBoolean(nodeName, false);
    }

    public static boolean getBoolean(String nodeName, boolean defult) {
        return getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).getBoolean(nodeName, defult);
    }

    public static int getInt(String nodeName) {
        return getInt(nodeName, 0);
    }

    public static int getInt(String nodeName, int defaultValue) {
        return getSharedPreferences(BaseApplication.getInstance(), SpConstant.FILE_NAME).getInt(nodeName, defaultValue);
    }

    public static void putSharePreFloat(Context context, String spName, String nodeName, float value) {
        Editor editor = getSharedPreferences(context, spName).edit();
        editor.putFloat(nodeName, value).commit();
    }

    public static Float getSharePreFloat(Context context, String spName, String nodeName, float defaultValue) {
        return getSharedPreferences(context, spName).getFloat(nodeName, defaultValue);
    }

}
