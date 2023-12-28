package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

public class StatisticsModel {

    @SerializedName("id")
    public String id;

    @SerializedName("match_fee")
    public double match_fee;

    @SerializedName("won_prize")
    public double won_prize;

    @SerializedName("win")
    public String win;

    @SerializedName("play_time")
    public String play_time;

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

    public double getWon_prize() {
        return won_prize;
    }

    public void setWon_prize(double won_prize) {
        this.won_prize = won_prize;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getPlay_time() {
        return play_time;
    }

    public void setPlay_time(String play_time) {
        this.play_time = play_time;
    }
}
