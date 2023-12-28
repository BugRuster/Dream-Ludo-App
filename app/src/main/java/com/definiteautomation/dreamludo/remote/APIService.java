package com.definiteautomation.dreamludo.remote;

import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.MyResponse;
import com.definiteautomation.dreamludo.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key="+ AppConstant.SERVER_KEY
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

