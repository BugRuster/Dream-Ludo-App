package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchModel {

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

        @SerializedName("play_time")
        private String play_time;

        @SerializedName("current_time")
        private String current_time;

        @SerializedName("start_time")
        private String start_time;

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

        public String getPlay_time() {
            return play_time;
        }

        public void setPlay_time(String play_time) {
            this.play_time = play_time;
        }

        public String getCurrent_time() {
            return current_time;
        }

        public void setCurrent_time(String current_time) {
            this.current_time = current_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }
    }

    @SerializedName("id")
    private String id;

    @SerializedName("match_fee")
    private double match_fee;

    @SerializedName("prize")
    private double prize;

    @SerializedName("table_size")
    private int table_size;

    @SerializedName("type")
    private int type;

    @SerializedName("play_time")
    private String play_time;

    @SerializedName("current_time")
    private String current_time;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("is_joined")
    private int is_joined;

    @SerializedName("win")
    private int win;

    @SerializedName("table_joined")
    private int table_joined;

    @SerializedName("result_status")
    private String result_status;

    @SerializedName("parti1_id")
    private String parti1_id;

    @SerializedName("parti2_id")
    private String parti2_id;

    @SerializedName("parti1_name")
    private String parti1_name;

    @SerializedName("parti2_name")
    private String parti2_name;

    @SerializedName("parti1_status")
    private String parti1_status;

    @SerializedName("parti2_status")
    private String parti2_status;

    @SerializedName("whatsapp_no1")
    private String whatsapp_no1;

    @SerializedName("whatsapp_no2")
    private String whatsapp_no2;

    @SerializedName("winner_name")
    private String winner_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMatch_fee() {
        return match_fee;
    }

    public void setMatch_fee(double match_fee) {
        this.match_fee = match_fee;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public int getTable_size() {
        return table_size;
    }

    public void setTable_size(int table_size) {
        this.table_size = table_size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlay_time() {
        return play_time;
    }

    public void setPlay_time(String play_time) {
        this.play_time = play_time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getIs_joined() {
        return is_joined;
    }

    public void setIs_joined(int is_joined) {
        this.is_joined = is_joined;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getTable_joined() {
        return table_joined;
    }

    public void setTable_joined(int table_joined) {
        this.table_joined = table_joined;
    }

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getParti1_id() {
        return parti1_id;
    }

    public void setParti1_id(String parti1_id) {
        this.parti1_id = parti1_id;
    }

    public String getParti2_id() {
        return parti2_id;
    }

    public void setParti2_id(String parti2_id) {
        this.parti2_id = parti2_id;
    }

    public String getParti1_name() {
        return parti1_name;
    }

    public void setParti1_name(String parti1_name) {
        this.parti1_name = parti1_name;
    }

    public String getParti2_name() {
        return parti2_name;
    }

    public void setParti2_name(String parti2_name) {
        this.parti2_name = parti2_name;
    }

    public String getParti1_status() {
        return parti1_status;
    }

    public void setParti1_status(String parti1_status) {
        this.parti1_status = parti1_status;
    }

    public String getParti2_status() {
        return parti2_status;
    }

    public void setParti2_status(String parti2_status) {
        this.parti2_status = parti2_status;
    }

    public String getWhatsapp_no1() {
        return whatsapp_no1;
    }

    public void setWhatsapp_no1(String whatsapp_no1) {
        this.whatsapp_no1 = whatsapp_no1;
    }

    public String getWhatsapp_no2() {
        return whatsapp_no2;
    }

    public void setWhatsapp_no2(String whatsapp_no2) {
        this.whatsapp_no2 = whatsapp_no2;
    }

    public String getWinner_name() {
        return winner_name;
    }

    public void setWinner_name(String winner_name) {
        this.winner_name = winner_name;
    }
}
