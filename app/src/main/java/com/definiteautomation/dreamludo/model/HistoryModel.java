package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;


public class HistoryModel {

    @SerializedName("msg")
    private String msg;

    @SerializedName("success")
    private int success;

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("payment_id")
    private String payment_id;

    @SerializedName("reg_name")
    private String reg_name;

    @SerializedName("reg_number")
    private String reg_number;

    @SerializedName("amount")
    private double amount;

    @SerializedName("payment_getway")
    private String payment_getway;

    @SerializedName("remark")
    private String remark;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("date_created")
    private String date_created;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_getway() {
        return payment_getway;
    }

    public void setPayment_getway(String payment_getway) {
        this.payment_getway = payment_getway;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
