package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {

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

        @SerializedName("id")
        private String id;

        @SerializedName("full_name")
        private String full_name;

        @SerializedName("profile_img")
        private String profile_img;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("country_code")
        private String country_code;

        @SerializedName("mobile")
        private String mobile;

        @SerializedName("whatsapp_no")
        private String whatsapp_no;

        @SerializedName("deposit_bal")
        private double deposit_bal;

        @SerializedName("won_bal")
        private double won_bal;

        @SerializedName("bonus_bal")
        private double bonus_bal;

        @SerializedName("fcm_token")
        private String fcm_token;

        @SerializedName("is_active")
        private int is_active;

        @SerializedName("is_block")
        private int is_block;


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

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getProfile_img() {
            return profile_img;
        }

        public void setProfile_img(String profile_img) {
            this.profile_img = profile_img;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getMobile() {
            return mobile;
        }

        public String getWhatsapp_no() {
            return whatsapp_no;
        }

        public void setWhatsapp_no(String whatsapp_no) {
            this.whatsapp_no = whatsapp_no;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getDeposit_bal() {
            return deposit_bal;
        }

        public void setDeposit_bal(double deposit_bal) {
            this.deposit_bal = deposit_bal;
        }

        public double getWon_bal() {
            return won_bal;
        }

        public void setWon_bal(double won_bal) {
            this.won_bal = won_bal;
        }

        public double getBonus_bal() {
            return bonus_bal;
        }

        public void setBonus_bal(double bonus_bal) {
            this.bonus_bal = bonus_bal;
        }

        public String getFcm_token() {
            return fcm_token;
        }

        public void setFcm_token(String fcm_token) {
            this.fcm_token = fcm_token;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public int getIs_block() {
            return is_block;
        }

        public void setIs_block(int is_block) {
            this.is_block = is_block;
        }
    }

}
