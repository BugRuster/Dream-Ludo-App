package com.definiteautomation.dreamludo.activity;

public class OrderData
{
    private String key;
    private String client_txn_id;
    private String amount;
    private String p_info;
    private String customer_name;
    private String customer_email;
    private String customer_mobile;
    private String redirect_url;
    private String udf1;
    private String udf2;
    private String udf3;

    public OrderData(String key, String client_txn_id, String amount, String p_info, String customer_name, String customer_email, String customer_mobile, String redirect_url, String udf1, String udf2, String udf3) {
        this.key = key;
        this.client_txn_id = client_txn_id;
        this.amount = amount;
        this.p_info = p_info;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_mobile = customer_mobile;
        this.redirect_url = redirect_url;
        this.udf1 = udf1;
        this.udf2 = udf2;
        this.udf3 = udf3;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClient_txn_id() {
        return client_txn_id;
    }

    public void setClient_txn_id(String client_txn_id) {
        this.client_txn_id = client_txn_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getP_info() {
        return p_info;
    }

    public void setP_info(String p_info) {
        this.p_info = p_info;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }
}
