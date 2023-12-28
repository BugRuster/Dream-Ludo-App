package com.definiteautomation.dreamludo;

import android.content.Context;
import android.content.SharedPreferences;

import com.definiteautomation.dreamludo.helper.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static Retrofit retrofit;
    public static Gson gson;

    public static MyApplication mInstance;
    public SharedPreferences preferences;
    public String prefName = "Ludo";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initRetrofit();
        initGson();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void saveIsNotification(boolean flag) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IsNotification", flag);
        editor.apply();
    }

    public boolean getNotification() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getBoolean("IsNotification", true);
    }

    private void initRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
    }


    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
