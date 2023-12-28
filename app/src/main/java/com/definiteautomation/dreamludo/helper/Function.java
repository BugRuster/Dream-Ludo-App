package com.definiteautomation.dreamludo.helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Function {

    public static void fireIntent(Context context, Class cls) {
        Intent i = new Intent(context, cls);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void fireIntentWithData(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void shareApp(Context context, String Code) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "Invite your friends to play our app and you'll get "+AppConstant.REFERRAL_PERCENTAGE+"% of the joining fees everytime your referred user join a paid contest above "+AppConstant.CURRENCY_SIGN+AppConstant.MIN_JOIN_LIMIT+". Refer code: " + Code + "\n\n\n App Link: https://play.google.com/store/apps/details?"+context.getPackageName());
        context.startActivity(Intent.createChooser(i, "Share"));
    }

}
