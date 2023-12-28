package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppModel {

    @SerializedName("result")
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result {

        @SerializedName("msg")
        private String msg;

        @SerializedName("success")
        private int success;

        @SerializedName("country_code")
        private String country_code;

        @SerializedName("currency_code")
        private String currency_code;

        @SerializedName("currency_sign")
        private String currency_sign;

        @SerializedName("paytm_mer_id")
        private String paytm_mer_id;

        @SerializedName("payu_id")
        private String payu_id;

        @SerializedName("payu_key")
        private String payu_key;

        @SerializedName("flutter_pub_key")
        private String flutter_pub_key;

        @SerializedName("flutter_sec_key")
        private String flutter_sec_key;

        @SerializedName("flutter_encry_key")
        private String flutter_encry_key;

        @SerializedName("flutter_currency_code")
        private String flutter_currency_code;

        @SerializedName("flutter_country_code")
        private String flutter_country_code;

        @SerializedName("min_entry_fee")
        private int min_entry_fee;

        @SerializedName("refer_percentage")
        private int refer_percentage;

        @SerializedName("maintenance_mode")
        private int maintenance_mode;

        @SerializedName("mop")
        private int mop;

        @SerializedName("wallet_mode")
        private int wallet_mode;

        @SerializedName("min_withdraw")
        private int min_withdraw;

        @SerializedName("max_withdraw")
        private int max_withdraw;

        @SerializedName("min_deposit")
        private int min_deposit;

        @SerializedName("max_deposit")
        private int max_deposit;

        @SerializedName("game_name")
        private String game_name;

        @SerializedName("package_name")
        private String package_name;

        @SerializedName("how_to_play")
        private String how_to_play;

        @SerializedName("cus_support_email")
        private String cus_support_email;

        @SerializedName("cus_support_mobile")
        private String cus_support_mobile;

        @SerializedName("force_update")
        private String force_update;

        @SerializedName("whats_new")
        private String whats_new;

        @SerializedName("update_date")
        private String update_date;

        @SerializedName("latest_version_name")
        private String latest_version_name;

        @SerializedName("latest_version_code")
        private String latest_version_code;

        @SerializedName("update_url")
        private String update_url;


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

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getCurrency_sign() {
            return currency_sign;
        }

        public void setCurrency_sign(String currency_sign) {
            this.currency_sign = currency_sign;
        }

        public String getPaytm_mer_id() {
            return paytm_mer_id;
        }

        public void setPaytm_mer_id(String paytm_mer_id) {
            this.paytm_mer_id = paytm_mer_id;
        }

        public String getPayu_id() {
            return payu_id;
        }

        public void setPayu_id(String payu_id) {
            this.payu_id = payu_id;
        }

        public String getPayu_key() {
            return payu_key;
        }

        public void setPayu_key(String payu_key) {
            this.payu_key = payu_key;
        }

        public String getFlutter_pub_key() {
            return flutter_pub_key;
        }

        public void setFlutter_pub_key(String flutter_pub_key) {
            this.flutter_pub_key = flutter_pub_key;
        }

        public String getFlutter_sec_key() {
            return flutter_sec_key;
        }

        public void setFlutter_sec_key(String flutter_sec_key) {
            this.flutter_sec_key = flutter_sec_key;
        }

        public String getFlutter_encry_key() {
            return flutter_encry_key;
        }

        public void setFlutter_encry_key(String flutter_encry_key) {
            this.flutter_encry_key = flutter_encry_key;
        }

        public String getFlutter_currency_code() {
            return flutter_currency_code;
        }

        public void setFlutter_currency_code(String flutter_currency_code) {
            this.flutter_currency_code = flutter_currency_code;
        }

        public String getFlutter_country_code() {
            return flutter_country_code;
        }

        public void setFlutter_country_code(String flutter_country_code) {
            this.flutter_country_code = flutter_country_code;
        }

        public int getMin_entry_fee() {
            return min_entry_fee;
        }

        public void setMin_entry_fee(int min_entry_fee) {
            this.min_entry_fee = min_entry_fee;
        }

        public int getRefer_percentage() {
            return refer_percentage;
        }

        public void setRefer_percentage(int refer_percentage) {
            this.refer_percentage = refer_percentage;
        }

        public int getMaintenance_mode() {
            return maintenance_mode;
        }

        public void setMaintenance_mode(int maintenance_mode) {
            this.maintenance_mode = maintenance_mode;
        }

        public int getMop() {
            return mop;
        }

        public void setMop(int mop) {
            this.mop = mop;
        }

        public int getWallet_mode() {
            return wallet_mode;
        }

        public void setWallet_mode(int wallet_mode) {
            this.wallet_mode = wallet_mode;
        }

        public int getMin_withdraw() {
            return min_withdraw;
        }

        public void setMin_withdraw(int min_withdraw) {
            this.min_withdraw = min_withdraw;
        }

        public int getMax_withdraw() {
            return max_withdraw;
        }

        public void setMax_withdraw(int max_withdraw) {
            this.max_withdraw = max_withdraw;
        }

        public int getMin_deposit() {
            return min_deposit;
        }

        public void setMin_deposit(int min_deposit) {
            this.min_deposit = min_deposit;
        }

        public int getMax_deposit() {
            return max_deposit;
        }

        public void setMax_deposit(int max_deposit) {
            this.max_deposit = max_deposit;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getHow_to_play() {
            return how_to_play;
        }

        public void setHow_to_play(String how_to_play) {
            this.how_to_play = how_to_play;
        }

        public String getCus_support_email() {
            return cus_support_email;
        }

        public void setCus_support_email(String cus_support_email) {
            this.cus_support_email = cus_support_email;
        }

        public String getCus_support_mobile() {
            return cus_support_mobile;
        }

        public void setCus_support_mobile(String cus_support_mobile) {
            this.cus_support_mobile = cus_support_mobile;
        }

        public String getForce_update() {
            return force_update;
        }

        public void setForce_update(String force_update) {
            this.force_update = force_update;
        }

        public String getWhats_new() {
            return whats_new;
        }

        public void setWhats_new(String whats_new) {
            this.whats_new = whats_new;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public String getLatest_version_name() {
            return latest_version_name;
        }

        public void setLatest_version_name(String latest_version_name) {
            this.latest_version_name = latest_version_name;
        }

        public String getLatest_version_code() {
            return latest_version_code;
        }

        public void setLatest_version_code(String latest_version_code) {
            this.latest_version_code = latest_version_code;
        }

        public String getUpdate_url() {
            return update_url;
        }

        public void setUpdate_url(String update_url) {
            this.update_url = update_url;
        }
    }
}
