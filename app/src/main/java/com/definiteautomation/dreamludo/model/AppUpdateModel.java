package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppUpdateModel {

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

        @SerializedName("id")
        private String id;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
