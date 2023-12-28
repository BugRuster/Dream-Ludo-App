package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurationModel {

    @SerializedName("result")
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result{

        @SerializedName("msg")
        private String msg;

        @SerializedName("success")
        private int success;

        @SerializedName("about_us")
        private String about_us;

        @SerializedName("faq")
        private String faq;

        @SerializedName("privacy_policy")
        private String privacy_policy;

        @SerializedName("legal_policy")
        private String legal_policy;

        @SerializedName("terms_condition")
        private String terms_condition;

        @SerializedName("rules")
        private String rules;

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

        public String getAbout_us() {
            return about_us;
        }

        public void setAbout_us(String about_us) {
            this.about_us = about_us;
        }

        public String getFaq() {
            return faq;
        }

        public void setFaq(String faq) {
            this.faq = faq;
        }

        public String getPrivacy_policy() {
            return privacy_policy;
        }

        public void setPrivacy_policy(String privacy_policy) {
            this.privacy_policy = privacy_policy;
        }

        public String getLegal_policy() {
            return legal_policy;
        }

        public void setLegal_policy(String legal_policy) {
            this.legal_policy = legal_policy;
        }

        public String getTerms_condition() {
            return terms_condition;
        }

        public void setTerms_condition(String terms_condition) {
            this.terms_condition = terms_condition;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }
    }

}
