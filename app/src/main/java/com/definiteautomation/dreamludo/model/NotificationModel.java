package com.definiteautomation.dreamludo.model;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("image")
    public String image;

    @SerializedName("url")
    public String url;

    @SerializedName("date_created")
    public String date_created;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

}
