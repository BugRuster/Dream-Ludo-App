package com.definiteautomation.dreamludo.helper;

import com.definiteautomation.dreamludo.remote.APIService;
import com.definiteautomation.dreamludo.remote.FCMRetrofitClient;

public class AppConstant {

    // Put your api url
    public static final String API_URL = "https://ciad.hub2technologies.com/";

    // Put your purchase key
    public static final String PURCHASE_KEY = "1234567890";

    // Put your FCM server key
    public static final String SERVER_KEY = "AAAAStJXfm4:APA91bHiY6iANudB8IYkZa19wyGvEbfMLkj0BEcZkLfvutEh5-W5P35UI6jLEBNhFni60YXEj6JkAsoD-SNWOaLP79tOTLk6SnZ0y_zUzPVDsV1P9Crgg3tfeBwimCsJ4uTsysr9kyc_";

    // ************************* Below value ca be change from Admin Panel *************************

    // Put your PayTm production merchant id
    public static String PAYTM_M_ID = "XXXXXXXXXXXXXXXXXXX";

    // Put your PayU production Merchant id & key
    public static String PAYU_M_ID = "XXXXXXXXXXXX";
    public static String PAYU_M_KEY = "XXXXXXXXXXX";

    // Set default country code, currency code and sign
    public static String COUNTRY_CODE = "+91";
    public static String CURRENCY_CODE = "INR";
    public static String CURRENCY_SIGN = "₹";

    // Set default app configuration
    public static int MAINTENANCE_MODE = 0;     // (0 for Off, 1 for On)
    public static int WALLET_MODE =  0;         // (0 for Enable, 1 for Disable)
    public static int MODE_OF_PAYMENT = 0;      // (0 for PayTm, 1 for PayU, 2 for RazorPay)

    // Set Refer Program
    public static int MIN_JOIN_LIMIT = 100;     // (In Amount)
    public static int REFERRAL_PERCENTAGE = 1;  // (In percentage)

    // Set withdraw limit (In Amount)
    public static int MIN_WITHDRAW_LIMIT = 100;
    public static int MAX_WITHDRAW_LIMIT = 5000;

    // Set deposit limit (In Amount)
    public static int MIN_DEPOSIT_LIMIT = 50;
    public static int MAX_DEPOSIT_LIMIT = 5000;

    // Set game name, package name, tutorial link and support email
    public static String GAME_NAME = "Ludo King";
    public static String PACKAGE_NAME = "com.ludo.king";
    public static String SUPPORT_EMAIL = "xxxxx@gmail.com";
    public static String SUPPORT_MOBILE = "0000000000";
    public static String HOW_TO_PLAY = "https://google.com";

    // ******************************* Don't change below value  ***********************************

    // PayU Production API Details
    public static final long API_CONNECTION_TIMEOUT = 1201;
    public static final long API_READ_TIMEOUT = 901;
    public static final String SERVER_MAIN_FOLDER = "";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "Global";

    // broadcast receiver intent filters
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // FCM URL
    private static final String FCM_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return FCMRetrofitClient.getClient(FCM_URL).create(APIService.class);
    }

    public interface IntentExtras {
        String ACTION_CAMERA = "action-camera";
        String ACTION_GALLERY = "action-gallery";
    }

    public interface PicModes {
        String CAMERA = "Camera";
        String GALLERY = "Gallery";
    }
}