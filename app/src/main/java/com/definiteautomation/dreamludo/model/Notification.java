package com.definiteautomation.dreamludo.model;


import java.util.Map;

public class Notification {
    private String title;
    private String body;
    private Map<String,String> data;
    private String click_action;

    public Notification() {
    }

    public Notification(String title, String body, String click_action) {
        this.title = title;
        this.body = body;
        this.click_action = click_action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }
}
