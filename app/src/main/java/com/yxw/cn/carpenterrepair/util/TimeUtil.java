package com.yxw.cn.carpenterrepair.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by CY on 2019/1/12
 */
public class TimeUtil {

    public static boolean compareTime(String orderTime) {
        try {
            long order = stringToLong(orderTime, "yyyy-MM-dd HH:mm:ss");
            long sys = dateToLong(new Date(System.currentTimeMillis()));
            return order >= sys;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static String reFreshTime(String orderTime) {
        try {
            long order = stringToLong(orderTime, "yyyy-MM-dd HH:mm:ss");
            long sys = dateToLong(new Date(System.currentTimeMillis()));
            if (order >= sys) {
                if (order-sys>1000 * 60 * 60 * 24){
                    SimpleDateFormat hms = new SimpleDateFormat("dd天HH时mm分ss秒");
                    hms.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date tTime = new Date(order - sys-1000 * 60 * 60 * 24);
                    String totalTime = hms.format(tTime);
                    return totalTime;
                }else {
                    SimpleDateFormat hms = new SimpleDateFormat("HH时mm分ss秒");
                    hms.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date tTime = new Date(order - sys);
                    String totalTime = hms.format(tTime);
                    return totalTime;
                }
            } else {
                return null;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static boolean compareTime2(String orderTime) {
        try {
            long order = stringToLong(orderTime, "yyyy-MM-dd HH:mm:ss")+ 60 * 60 * 1000;
            long sys = dateToLong(new Date(System.currentTimeMillis()));
            return order >= sys;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static String reFreshTime2(String orderTime) {
        try {
            long order = stringToLong(orderTime, "yyyy-MM-dd HH:mm:ss")+ 60 * 60 * 1000;
            long sys = dateToLong(new Date(System.currentTimeMillis()));
            if (order >= sys) {
                if (order-sys>1000 * 60 * 60 * 24){
                    SimpleDateFormat hms = new SimpleDateFormat("dd天HH时mm分ss秒");
                    hms.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date tTime = new Date(order - sys-1000 * 60 * 60 * 24);
                    String totalTime = hms.format(tTime);
                    return totalTime;
                }else {
                    SimpleDateFormat hms = new SimpleDateFormat("HH时mm分ss秒");
                    hms.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date tTime = new Date(order - sys);
                    String totalTime = hms.format(tTime);
                    return totalTime;
                }
            } else {
                return null;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        return formatter.format(data);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

}
