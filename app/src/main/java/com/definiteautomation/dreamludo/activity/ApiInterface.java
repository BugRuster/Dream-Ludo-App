package com.definiteautomation.dreamludo.activity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface
{
    @POST("create_order")
    Call<ApiResponse> createOrder(@Body OrderData orderData);
}
