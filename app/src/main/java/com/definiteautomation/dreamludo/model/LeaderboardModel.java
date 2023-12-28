package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

public class LeaderboardModel {

    @SerializedName("full_name")
    public String full_name;

    @SerializedName("won_prize")
    public double won_prize;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public double getWon_prize() {
        return won_prize;
    }

    public void setWon_prize(double won_prize) {
        this.won_prize = won_prize;
    }
}
