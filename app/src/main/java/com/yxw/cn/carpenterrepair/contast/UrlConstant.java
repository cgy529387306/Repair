package com.yxw.cn.carpenterrepair.contast;

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

    public final static String LOGIN = BASE_USER + "/gateway/app/api/loginByPwd";
    public final static String QUICK_LOGIN = BASE_USER + "/gateway/app/api/loginByCode";
    public final static String REGISTER = BASE_USER + "/gateway/app/api/register";
    public final static String REFRESH_TOKEN = BASE_USER + "/gateway/app/refreshToken";

    public final static String FORGET_PASSWORD = BASE_USER + "/ms70/api/user/auth/forgetPwd";//忘记密码
    public final static String MODIFY_PASSWORD = BASE_USER + "/ms70/api/user/info/updatePwd";
    public final static String CHANGE_AVATAR = BASE_USER + "/ms70/api/user/info/updateAvatar";
    public final static String CHANGE_USERINFO = BASE_USER + "/ms70/api/user/info/update";
    public final static String GET_WORKER_INFO = BASE_USER + "/ms70/api/user/info/view";
    public final static String USER_FEEDBACK = BASE_USER + "/ms70/api/user/info/userFeedback";//用户反馈
    public final static String UPLOAD_IDCARD = BASE_USER + "/ms70/api/user/info/uploadIdCard";//身份证上传
    public final static String MY_SERVICE_LIST = BASE_USER + "/ms70/api/user/info/childService/{bindingCode}";//我的工程师列表
    public final static String MY_SERVICE_CHECK_LIST = BASE_USER + "/ms70/api/user/info/findAllByApplicationStart";//工程师加入服务商审核列表
    public final static String JOIN_SERVICE = BASE_USER + "/ms70/api/user/info/joinService/";//工程师加入服务商
    public final static String PARENT_SERVICE = BASE_USER + "/ms70/api/user/info/parentService/{bindingCode}";//隶属服务商
    public final static String SAVE_CATEGORY = BASE_USER + "/ms70/api/user/info/saveProject";//新增用户项目
    public final static String UPDATE_ALIPAY_ACCOUNT = BASE_USER + "/ms70/api/user/info/saveAliplay/";//1015 - 用户 - 个人资料 - 修改支付宝账号
    public final static String APPLY_WITHDRAWAL = BASE_USER + "/ms70/api/user/info/destoonFinanceCash";//用户提现申请
    public final static String APPLY_WITHDRAWAL_LIST = BASE_USER + "/ms70/api/fix/user/transaction/record/findAll";//交易明细

    public final static String GET_CODE = BASE_SERVICE + "/ms20/api/validateCode/code/ingnore-internal/getCode";
    public final static String GET_CODE_REGISTER = BASE_SERVICE + "/ms20/api/validateCode/code/ingnore-internal/getRegisterCode";
    public final static String GET_ALL_CATEGORY = BASE_USER + "/ms20/api/sys/dicts/categoryList";//获取项目分类列表
    public final static String GET_ALL_REGION = BASE_USER + "/ms20/api/sys/region/allRegionTree";//获取所有地区接口
    public final static String GET_EXCEPTION_REASON = BASE_USER + "/ms20/api/sys/dicts/findDictsByKey";//查询数据字典


    public final static String ORDER_ARRIVAL = BASE_USER + "/ms60/api/fix/order/accept/confirmArrival";//工程师确认到场
    public final static String ORDER_FINISH = BASE_USER + "/ms60/api/fix/order/accept/endServiceBycode";//工程师服务完成
    public final static String ORDER_RECEIVE = BASE_USER + "/ms60/api/fix/order/accept/receive/";//工程师确认接单
    public final static String ORDER_RESERVATION = BASE_USER + "/ms60/api/fix/order/accept/reservation";//工程师预约上门时间
    public final static String ORDER_TURN_RESERVATION = BASE_USER + "/ms60/api/fix/order/accept/turnReservation";//工程师开始服务
    public final static String ORDER_LIST = BASE_USER + "/ms60/api/fix/order/findEngineerOrderList";//订单条件查询
    public final static String ORDER_DETAIL = BASE_USER + "/ms60/api/fix/order/orderDetails/";//获取订单详情
    public final static String ORDER_EXEPTION_APPOINT = BASE_USER + "/ms60/api/fix/order/exception/appointment";//预约异常
    public final static String ORDER_EXEPTION_SIGN = BASE_USER + "/ms60/api/fix/order/exception/signIn";//签到异常
    public final static String ORDER_STATUS_LIST = BASE_USER + "/ms60/api/fix/order/getOrderStatusList";//getOrderStatusList
    public final static String ORDER_CANCEL = BASE_USER + "/ms60/api/fix/order/accept/cancelReceiveOrder/{orderId}";//工程师取消接单
    public final static String ORDER_START = BASE_USER + "/ms60/api/fix/order/accept/startService/{orderId}";//工程师开始服务


    public final static String GET_REGION_TREE = BASE_URL + "/api/region/tree";
    public final static String GET_SERVICE_FEE = BASE_URL + "/api/region/serviceFee";
    public final static String ORDER = BASE_URL + "/api/fix/order/submit";




    public final static String WECHAT_PAY_ORDER = BASE_URL + "/api/fix/pay/wxPrepay";
    public final static String USER_EVALUATE = BASE_URL + "/api/fix/userOrder/evaluate";
    public final static String USER_COMPLAIN = BASE_URL + "/api/fix/userOrder/complain";
    public final static String QUERT_LIST_BY_MARK = BASE_URL + "/api/dicts/queryListByMarkWithAll";//通用 - 根据标识mark,获取数据字典
    public final static String EDIT_TRAIT = BASE_URL + "/api/fix/shopUser/editTrait";//用户 - 个人资料-修改常驻地点和服务时间
    public final static String CHECK_ALI_PAY = BASE_URL + "/api/fix/aliplay/checkAlipay";//1013 - 用户 - 取消订单,未上门
    public final static String RONG_CLOUD_TOKEN = BASE_URL + "/api/rongCloud/getToken";//3010 - 通用 - 融云即时通讯（佳信客服）-获取Token
    public final static String RESERVATION_TIME = BASE_URL + "/api/fix/workerOrder/reservation";//2020 - 维修人员 - 预约上门时间

}
