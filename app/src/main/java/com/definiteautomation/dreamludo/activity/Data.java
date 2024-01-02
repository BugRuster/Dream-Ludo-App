package com.definiteautomation.dreamludo.activity;

public class Data
{
    private int order_id;
    private String payment_url;
    private String upi_id_hash;

    public Data(int order_id, String payment_url, String upi_id_hash) {
        this.order_id = order_id;
        this.payment_url = payment_url;
        this.upi_id_hash = upi_id_hash;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getUpi_id_hash() {
        return upi_id_hash;
    }

    public void setUpi_id_hash(String upi_id_hash) {
        this.upi_id_hash = upi_id_hash;
    }
}
