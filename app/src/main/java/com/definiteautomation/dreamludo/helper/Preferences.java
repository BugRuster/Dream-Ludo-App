package com.definiteautomation.dreamludo.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.definiteautomation.dreamludo.activity.LoginActivity;

public class Preferences {

    // SharedPreferences file name
    private static final String PREF_NAME = "com.codezon.ludofantacy";

    // private static variables
    @SuppressLint("StaticFieldLeak")
    private static Preferences instance;

    public Context context;

    // Shared Preferences
    private final SharedPreferences sharedPref;

    // The constant KEY_USER_ID
    public static final String KEY_USER_ID = "KEY_USER_ID";

    // The constant KEY_USERNAME
    public static final String KEY_USERNAME = "KEY_USERNAME";

    // The constant KEY_FULL_NAME
    public static final String KEY_FULL_NAME = "KEY_FULL_NAME";

    // The constant KEY_PROFILE_PHOTO
    public static final String KEY_PROFILE_PHOTO = "KEY_PROFILE_PHOTO";

    // The constant KEY_EMAIL
    public static final String KEY_EMAIL = "KEY_EMAIL";

    // The constant KEY_COUNTRY_CODE
    public static final String KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE";

    // The constant KEY_MOBILE
    public static final String KEY_MOBILE = "KEY_MOBILE";

    // The constant KEY_WHATSAPP
    public static final String KEY_WHATSAPP = "KEY_WHATSAPP";

    // The constant KEY_PASSWORD
    public static final String KEY_PASSWORD = "KEY_PASSWORD";

    // The constant KEY_REFER_CODE
    public static final String KEY_REFER_CODE = "KEY_REFER_CODE";

    // The constant KEY_IS_AUTO_LOGIN
    public static final String KEY_IS_AUTO_LOGIN = "KEY_IS_AUTO_LOGIN";


    /**
     * Instantiates a new Preferences.
     *
     * @param context the context
     */
    public Preferences(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets instance.
     *
     * @param c the c
     * @return the instance
     */
    public static Preferences getInstance(Context c) {
        if (instance == null) {
            instance = new Preferences(c);
        }
        return instance;
    }

    /**
     * Gets shared pref.
     *
     * @return the shared pref
     */
    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     */
    public String getString(String key) {
        return sharedPref.getString(key, "");
    }

    /**
     * Sets string.
     *
     * @param key   the key
     * @param value the value
     */
    public void setString(String key, String value) {
        SharedPreferences.Editor sharedPrefEditor = getSharedPref().edit();
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.apply();
    }

    public void setlogout() {
        SharedPreferences.Editor sharedPrefEditor = getSharedPref().edit();
        sharedPrefEditor.clear();
        sharedPrefEditor.apply();

        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra("finish", true);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
