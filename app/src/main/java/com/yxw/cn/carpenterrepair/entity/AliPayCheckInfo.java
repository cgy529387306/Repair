package com.yxw.cn.carpenterrepair.entity;

public class AliPayCheckInfo {

    private AlipayCheckResponse alipay_trade_query_response;
    private String sign;

    public AlipayCheckResponse getAlipay_trade_query_response() {
        return alipay_trade_query_response;
    }

    public void setAlipay_trade_query_response(AlipayCheckResponse alipay_trade_query_response) {
        this.alipay_trade_query_response = alipay_trade_query_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
