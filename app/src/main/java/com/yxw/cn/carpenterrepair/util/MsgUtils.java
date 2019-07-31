package com.yxw.cn.carpenterrepair.util;

import com.yxw.cn.carpenterrepair.entity.NoticeBean;

import java.util.ArrayList;
import java.util.List;

public class MsgUtils {


    public static void saveMsg(List<NoticeBean> dataList){
        try {
            boolean isFirstSave = SpUtil.getBoolean("isFirstSave",true);
            List<String> result = new ArrayList<>();
            String idList = SpUtil.getStr("idList");
            if (Helper.isNotEmpty(dataList)){
                for (NoticeBean noticeBean:dataList){
                    if (isFirstSave){
                        result.add(noticeBean.getNoticeId());
                    }else{
                        if (Helper.isNotEmpty(idList) && !idList.contains(noticeBean.getNoticeId()+",")){
                            result.add(noticeBean.getNoticeId());
                        }
                    }
                }
            }
            String addList = listToStr(result);
            String allList = idList+addList;
            SpUtil.putStr("idList",allList);
            SpUtil.putBoolean("isFirstSave",false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getMsgCount(){
        try {
            String idList = SpUtil.getStr("idList");
            if (Helper.isEmpty(idList)){
                return 0;
            }else{
                String[] dataArray = idList.split(",");
                return dataArray.length;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static void deleteMsg(String msgId){
        if (Helper.isEmpty(msgId)){
            return;
        }
        try {
            String idList = SpUtil.getStr("idList");
            idList = idList.replace(msgId+",","");
            SpUtil.putStr("idList",idList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean hasRead(String msgId){
        if (Helper.isEmpty(msgId)){
            return false;
        }
        try {
            String idList = SpUtil.getStr("idList");
            return !idList.contains(msgId+",");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * List 转 string分隔
     * @param dataList
     * @return
     */
    public static String listToStr(List<String> dataList){
        if (Helper.isEmpty(dataList)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            sb.append(dataList.get(i)).append(",");

        }
        return sb.toString();
    }
}
