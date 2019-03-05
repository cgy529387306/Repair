package com.yxw.cn.carpenterrepair.entity;

public class AlipayCheckResponse {

    private String msg;
    private String code;
    private String out_trade_no;
    private String sub_msg;
    private String sub_code;
    private String receipt_amount;
    private String point_amount;
    private String buyer_pay_amount;
    private String invoice_amount;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(String point_amount) {
        this.point_amount = point_amount;
    }

    public String getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(String buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }
}
