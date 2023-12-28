package com.definiteautomation.dreamludo.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.activity.HistoryActivity;
import com.definiteautomation.dreamludo.activity.LoginActivity;
import com.definiteautomation.dreamludo.activity.MainActivity;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Integer notificationId = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        try {
            Map<String, String> data = remoteMessage.getData();
            String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
            String img_uri = String.valueOf(remoteMessage.getNotification().getImageUrl());

            String action = data.get("action");
            Log.i(TAG, "onMessageReceived: title : " + title);
            Log.i(TAG, "onMessageReceived: message : " + message);
            Log.i(TAG, "onMessageReceived: imageUrl : " + img_uri);
            Log.i(TAG, "onMessageReceived: action : " + action);

            if (img_uri.equals("null")) {
                showNotificationMessage(title, message, click_action);
            } else {
                showNotificationMessageWithBigImage(title, message, click_action, img_uri);
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String click_action = data.getString("click_action");
            //boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            //String timestamp = data.getString("timestamp");
            //String url = data.getString("external_link");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            //Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload);
            Log.e(TAG, "imageUrl: " + imageUrl);
            //Log.e(TAG, "timestamp: " + timestamp);
            //Log.e(TAG, "externalLink: " + url);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppConstant.PUSH_NOTIFICATION);
                pushNotification.putExtra("click_action", click_action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils();
                notificationUtils.playNotificationSound(getApplicationContext());
            } else {
                // app is in background, show the notification in notification tray

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(title, message, click_action);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(title, message, click_action, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(String title, String message, String action) {

        Intent resultIntent;

        if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1") && Objects.equals(action, "MainActivity")) {
            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("click_action", action);
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1") && Objects.equals(action, "HistoryActivity")) {
            resultIntent = new Intent(getApplicationContext(), HistoryActivity.class);
            resultIntent.putExtra("click_action", action);
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1")) {
            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("click_action", "default");
        }
        else {
            resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
            resultIntent.putExtra("click_action", "default");
        }

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSound(notificationSound)
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setColor(getResources().getColor(android.R.color.holo_blue_dark));

        if (notificationManager != null) {
            notificationManager.notify(incrementNotificationId(), builder.build());
            notificationManager.cancel(incrementNotificationId());
        }
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(String title, String message, String action, String imageUrl) {

        Intent resultIntent;

        if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1") && Objects.equals(action, "MainActivity")) {
            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("click_action", action);
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1") && Objects.equals(action, "HistoryActivity")) {
            resultIntent = new Intent(getApplicationContext(), HistoryActivity.class);
            resultIntent.putExtra("click_action", action);
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1")) {
            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("click_action", "default");
        }
        else {
            resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
            resultIntent.putExtra("click_action", "default");
        }

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(true);
            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSound(notificationSound)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromUrl(imageUrl)).setSummaryText(message))
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setColor(getResources().getColor(android.R.color.holo_blue_dark));

        if (notificationManager != null) {
            notificationManager.notify(incrementNotificationId(), builder.build());
            notificationManager.cancel(incrementNotificationId());
        }
    }


    private Integer incrementNotificationId() {
        return notificationId++;
    }

    public static Bitmap getBitmapFromUrl(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
