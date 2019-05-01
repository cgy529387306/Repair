package com.yxw.cn.carpenterrepair.contast;

import com.yxw.cn.carpenterrepair.entity.CurrentUser;

public class UrlConstant {

    public final static int mRoleSign = 1;  // 1工程师端 3服务商端

    public final static String URL_SHOPPING = "http://47.98.53.48:8080/mall/index";

//    public final static String BASE_URL = "http://result.eolinker.com/LXY3SzS37282f67e2e5a56f813e0030bf70d292f0f69e16?uri=";
    public final static String BASE_FILE_DOMIN = "http://114.115.137.33:880/";
//    public final static String BASE_DOMIN = "http://114.115.137.33:80";
    public final static String BASE_DOMIN = "http://39.98.73.166:8080";
//    public final static String BASE_URL = BASE_DOMIN+"platform";
//    public final static String BASE_URL = BASE_DOMIN+"/jxdj";
    public final static String BASE_URL = BASE_DOMIN;
//    public final static String BASE_URL = "http://47.98.53.48:8080";
//    public final static String BASE_URL = "http://8080.port.doublev.cn";
//    public final static String BASE_URL2 = "http://47.98.53.48:8080";


    public final static String BASE_USER = "http://114.115.184.217:28010";
    public final static String BASE_SERVICE = "http://114.115.184.217:28020";
    public final static String BASE_WORKER = "http://114.115.184.217:28070";

    public final static String GET_CODE = BASE_SERVICE + "/gateway/ms20/api/validateCode/code/getCode";
    public final static String LOGIN = BASE_USER + "/gateway/app/api/loginByPwd";
    public final static String QUICK_LOGIN = BASE_USER + "/gateway/app/api/loginByCode";
    public final static String REGISTER = BASE_USER + "/gateway/app/api/register";

    public final static String FORGET_PASSWORD = BASE_WORKER + "/ms70/api/user/auth/forgetPwd";//忘记密码
    public final static String MODIFY_PASSWORD = BASE_WORKER + "/ms70/api/user/info/updatePwd";
    public final static String CHANGE_AVATAR = BASE_WORKER + "/ms70/api/user/info/updateAvatar";
    public final static String CHANGE_USERINFO = BASE_WORKER + "/ms70/api/user/info/update";
    public final static String GET_WORKER_INFO = BASE_WORKER + "/ms70/api/user/info/view/"+ CurrentUser.getInstance().getUserId();
    public final static String USER_FEEDBACK = BASE_WORKER + "/ms70/api/user/info/userFeedback";//用户反馈
    public final static String UPLOAD_IDCARD = BASE_WORKER + "/ms70/api/user/info/uploadIdCard";//身份证上传


    public final static String GET_ALL_CATEGORY = BASE_URL + "/api/fix/category/all";
    public final static String GET_REGION_TREE = BASE_URL + "/api/region/tree";
    public final static String GET_SERVICE_FEE = BASE_URL + "/api/region/serviceFee";
    public final static String ORDER = BASE_URL + "/api/fix/order/submit";




    public final static String CHANGE_NAME = BASE_URL + "/api/fix/shopUser/editUeserNickname";
    public final static String CHANGE_MOBILE = BASE_URL + "/api/fix/shopUser/editUeserMobile";
    public final static String WECHAT_PAY_ORDER = BASE_URL + "/api/fix/pay/wxPrepay";
    public final static String WORKER_ORDER_LIST = BASE_URL + "/api/fix/order/worker/list";
    public final static String WORKER_WAIT_ORDER_LIST = BASE_URL + "/api/fix/order/wait/list";
    public final static String USER_EVALUATE = BASE_URL + "/api/fix/userOrder/evaluate";
    public final static String USER_COMPLAIN = BASE_URL + "/api/fix/userOrder/complain";
    public final static String USER_ORDER_LIST = BASE_URL + "/api/fix/order/client/list";//用户下单列表
    public final static String ORDER_VIEW = BASE_URL + "/api/fix/order/view";//订单详情
    public final static String GET_ASSETS = BASE_URL + "/api/fix/shopUser/getAssets";//获取用户/工程师资产
    public final static String RECEIVE_ORDER = BASE_URL + "/api/fix/order/receive";//维修人员-接单
    public final static String CONFIRM_FEE = BASE_URL + "/api/fix/workerOrder/confirmFee";//维修人员 - 现场评估维修费用
    public final static String QUERT_LIST_BY_MARK = BASE_URL + "/api/dicts/queryListByMarkWithAll";//通用 - 根据标识mark,获取数据字典
    public final static String EDIT_GOOD_CATEGORY = BASE_URL + "/api/fix/shopUser/editGoodCategory";//维修人员 - 修改擅长项目
    public final static String EDIT_TRAIT = BASE_URL + "/api/fix/shopUser/editTrait";//用户 - 个人资料-修改常驻地点和服务时间
    public final static String CANCEL_ORDER_ARRIVALED = BASE_URL + "/api/fix/userOrder/cancelOrderArrivaled";//1013 - 用户 - 取消订单,已上门
    public final static String CANCEL_ORDER_UNARRIVALED = BASE_URL + "/api/fix/userOrder/cancelOrderUnArrivaled";//1013 - 用户 - 取消订单,未上门
    public final static String CHECK_ALI_PAY = BASE_URL + "/api/fix/aliplay/checkAlipay";//1013 - 用户 - 取消订单,未上门
    public final static String WORKER_DEPOSIT = BASE_URL + "/api/fix/workerOrder/deposit";//2012 - 维修人员 - 提现
    public final static String CONFIRM_ARRIVAL_SMS = BASE_URL + "/api/fix/workerOrder/confirmArrivalSMS";//2013 - 维修人员 - 确认到场(发送短信验证码)
    public final static String START_SERVICE_SMS = BASE_URL + "/api/fix/workerOrder/startServiceSMS";//2014 - 维修人员 - 开始服务(需短信验证)
    public final static String END_SERVICE_CODE = BASE_URL + "/api/fix/workerOrder/getEndServiceCode";//2015 - 维修人员 - 服务完成请求短信验证接口
    public final static String END_SERVICE_BY_CODE = BASE_URL + "/api/fix/workerOrder/endServiceBycode";//2016 - 维修人员 - 服务完成短信验证提交接口
    public final static String RONG_CLOUD_TOKEN = BASE_URL + "/api/rongCloud/getToken";//3010 - 通用 - 融云即时通讯（佳信客服）-获取Token
    public final static String UPDATE_ALIPAY_ACCOUNT = BASE_URL + "/api/fix/shopUser/editAliplayAccount";//1015 - 用户 - 个人资料 - 修改支付宝账号
    public final static String WORKER_CANCEL_ORDER = BASE_URL + "/api/fix/workerOrder/cancelOrder";//工人取消订单
    public final static String RESERVATION_TIME = BASE_URL + "/api/fix/workerOrder/reservation";//2020 - 维修人员 - 预约上门时间
    public final static String CONFIRM_ARRIVAL = BASE_URL + "/api/fix/workerOrder/confirmArrival";//2004 - 维修人员 - 确认到达现场
    public final static String ABNORMAL_TYPE = BASE_URL + "/api/dicts/queryListByMark";//3004 - 通用 - 根据标识mark,获取数据字典
    public final static String ABNORMAL_COMMIT = BASE_URL + "/api/fix/workerOrder/turnReservation";//2021 - 维修人员 - 预约异常改约
    public final static String SERVICE_TYPE = BASE_URL + "/api/fix/category/fixUserCategory";//2019 - 维修人员 - 获取服务分类
    public final static String CONTACT_CLIENT = BASE_URL + "/api/fix/workerOrder/contactClient";//2022 - 维修人员 - 添加联系客户次数

}
