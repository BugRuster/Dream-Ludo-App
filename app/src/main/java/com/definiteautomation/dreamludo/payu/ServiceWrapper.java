package com.definiteautomation.dreamludo.payu;

import com.definiteautomation.dreamludo.BuildConfig;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceWrapper {

    private final ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(AppConstant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(AppConstant.API_READ_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(AppConstant.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
    }

    public Call<String> newHashCall( String key, String txtid, String amount, String productinfo, String fullname, String email){
       return mServiceInterface.getHashCall(
               convertPlainString(key),   convertPlainString(txtid),convertPlainString(amount),
               convertPlainString(productinfo), convertPlainString( fullname),  convertPlainString(email));
    }

      // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }
}


