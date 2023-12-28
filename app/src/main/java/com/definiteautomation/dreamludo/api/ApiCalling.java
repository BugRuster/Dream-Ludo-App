package com.definiteautomation.dreamludo.api;

import com.definiteautomation.dreamludo.model.AppModel;
import com.definiteautomation.dreamludo.model.ConfigurationModel;
import com.definiteautomation.dreamludo.model.HistoryModel;
import com.definiteautomation.dreamludo.model.LeaderboardModel;
import com.definiteautomation.dreamludo.model.MatchModel;
import com.definiteautomation.dreamludo.model.NotificationModel;
import com.definiteautomation.dreamludo.model.StatisticsModel;
import com.definiteautomation.dreamludo.model.Token;
import com.definiteautomation.dreamludo.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiCalling {

    //@Multipart
    @FormUrlEncoded
    @POST("paytmallinone/init_Transaction.php")
    Call<Token> generateTokenCall(
            @Field("code") String language,
            @Field("MID") String mid,
            @Field("ORDER_ID") String order_id,
            @Field("AMOUNT") String amount
    );

    @FormUrlEncoded
    @POST(ApiConstant.POST_USER_REGISTRATION_WITH_REFER)
    Call<UserModel> customerRegistrationWithRefer(
            @Field("purchase_key") String purchase_key,
            @Field("full_name") String full_name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("country_code") String country_code,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("fcm_token") String fcm_token,
            @Field("device_id") String device_id,
            @Field("referer") String referer);

    @FormUrlEncoded
    @POST(ApiConstant.POST_USER_REGISTRATION_WITHOUT_REFER)
    Call<UserModel> customerRegistrationWithoutRefer(
            @Field("purchase_key") String purchase_key,
            @Field("full_name") String full_name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("country_code") String country_code,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("fcm_token") String fcm_token,
            @Field("device_id") String device_id);



    @GET(ApiConstant.GET_USER_LOGIN)
    Call<UserModel> loginUser(
            @Query("purchase_key") String purchase_key,
            @Query("username") String username,
            @Query("password") String password,
            @Query("type") String type);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_USER_DETAILS)
    Call<UserModel> getUserDetails(
            @Query("purchase_key") String purchase_key,
            @Query("id") String id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_APP_DETAILS)
    Call<AppModel> getAppDetails(
            @Query("purchase_key") String purchase_key);



    @GET(ApiConstant.GET_NOTIFICATION)
    Call<List<NotificationModel>> getNotification(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_HISTORY)
    Call<List<HistoryModel>> getHistory(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_STATISTICS)
    Call<List<StatisticsModel>> getStatistics(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);

    @GET(ApiConstant.GET_LEADERBOARD)
    Call<List<LeaderboardModel>> getLeaderboard(
            @Query("purchase_key") String purchase_key);



    @FormUrlEncoded
    @POST(ApiConstant.POST_UPDATE_PROFILE)
    Call<UserModel> updateUserProfileInfo(
            @Field("purchase_key") String purchase_key,
            @Field("id") String id,
            @Field("full_name") String full_name,
            @Field("whatsapp_no") String whatsapp_no);

    @FormUrlEncoded
    @POST(ApiConstant.POST_UPDATE_PROFILE)
    Call<UserModel> updateUserProfilePassword(
            @Field("purchase_key") String purchase_key,
            @Field("id") String id,
            @Field("password") String password);

    @FormUrlEncoded
    @POST(ApiConstant.POST_UPDATE_PROFILE)
    Call<UserModel> updateUserProfileToken(
            @Field("purchase_key") String purchase_key,
            @Field("id") String id,
            @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST(ApiConstant.POST_UPDATE_PHOTO)
    Call<UserModel> updateUserPicture(
            @Field("purchase_key") String purchase_key,
            @Field("id") String id,
            @Field("profile_img") String profile_img);

    @FormUrlEncoded
    @POST(ApiConstant.POST_RESET_PASSWORD)
    Call<UserModel> userResetPassword(
            @Field("purchase_key") String purchase_key,
            @Field("mobile") String mobile,
            @Field("password") String password);



    @GET(ApiConstant.GET_VERIFY_REGISTER)
    Call<UserModel> verifyUserRegister(
            @Query("purchase_key") String purchase_key,
            @Query("device_id") String device_id,
            @Query("mobile") String mobile,
            @Query("email") String email,
            @Query("username") String username);

    @GET(ApiConstant.GET_VERIFY_MOBILE)
    Call<UserModel> verifyUserMobile(
            @Query("purchase_key") String purchase_key,
            @Query("mobile") String mobile);

    @GET(ApiConstant.GET_VERIFY_REFER)
    Call<UserModel> verifyUserRefer(
            @Query("purchase_key") String purchase_key,
            @Query("refer") String refer);


    @FormUrlEncoded
    @POST(ApiConstant.POST_BALANCE)
    Call<UserModel> postBalance(
            @Field("purchase_key") String purchase_key,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("payment_id") String payment_id,
            @Field("checksum") String checksum,
            @Field("amount") double amount,
            @Field("payment_getway") String payment_getway);

    @FormUrlEncoded
    @POST(ApiConstant.POST_DEPOSIT)
    Call<UserModel> postDeposit(
            @Field("purchase_key") String purchase_key,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("payment_id") String payment_id,
            @Field("checksum") String checksum,
            @Field("amount") double amount,
            @Field("payment_getway") String payment_getway);

    @FormUrlEncoded
    @POST(ApiConstant.POST_WITHDRAW)
    Call<UserModel> postWithdraw(
            @Field("purchase_key") String purchase_key,
            @Field("user_id") String user_id,
            @Field("reg_name") String reg_name,
            @Field("reg_number") String reg_number,
            @Field("amount") double amount,
            @Field("payment_getway") String payment_getway);


    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_MATCH_UPCOMING)
    Call<List<MatchModel>> getMatchUpcoming(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_MATCH_ONGOING)
    Call<List<MatchModel>> getMatchOngoing(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_MATCH_COMPLETED)
    Call<List<MatchModel>> getMatchCompleted(
            @Query("purchase_key") String purchase_key,
            @Query("user_id") String user_id);


    @FormUrlEncoded
    @POST(ApiConstant.POST_RESULT)
    Call<MatchModel> updateResultParti1WithProof(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("user_id") String user_id,
            @Field("parti1_status") String parti1_status,
            @Field("parti1_proof") String parti1_proof);

    @FormUrlEncoded
    @POST(ApiConstant.POST_RESULT)
    Call<MatchModel> updateResultParti1WithoutProof(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("parti1_status") String parti1_status);

    @FormUrlEncoded
    @POST(ApiConstant.POST_RESULT)
    Call<MatchModel> updateResultParti2WithProof(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("user_id") String user_id,
            @Field("parti2_status") String parti2_status,
            @Field("parti2_proof") String parti2_proof);

    @FormUrlEncoded
    @POST(ApiConstant.POST_RESULT)
    Call<MatchModel> updateResultParti2WithoutProof(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("parti2_status") String parti2_status);


    @FormUrlEncoded
    @POST(ApiConstant.POST_JOIN_MATCH)
    Call<MatchModel> postParticipant1Result(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("parti1") String parti1);

    @FormUrlEncoded
    @POST(ApiConstant.POST_JOIN_MATCH)
    Call<MatchModel> postParticipant2Result(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("parti2") String parti2);

    @FormUrlEncoded
    @POST(ApiConstant.DELETE_PARTICIPANT)
    Call<MatchModel> deleteParticipant(
            @Field("purchase_key") String purchase_key,
            @Field("match_id") String match_id,
            @Field("parti1") String parti1);

    @GET(ApiConstant.SEARCH_PARTICIPANT)
    Call<List<MatchModel>> searchParticipant(
            @Query("purchase_key") String purchase_key,
            @Query("match_id") String match_id);


    @GET(ApiConstant.GET_PRIVACY_POLICY)
    Call<ConfigurationModel> getPrivacyPolicy(
            @Query("purchase_key") String purchase_key);

    @GET(ApiConstant.GET_TERMS_CONDITION)
    Call<ConfigurationModel> getTermsCondition(
            @Query("purchase_key") String purchase_key);

    @GET(ApiConstant.GET_LEGAL_POLICY)
    Call<ConfigurationModel> getLegalPolicy(
            @Query("purchase_key") String purchase_key);

    @GET(ApiConstant.GET_ABOUT_US)
    Call<ConfigurationModel> getAboutUs(
            @Query("purchase_key") String purchase_key);

    @GET(ApiConstant.GET_FAQ)
    Call<ConfigurationModel> getFAQ(
            @Query("purchase_key") String purchase_key);

    @GET(ApiConstant.GET_RULES)
    Call<ConfigurationModel> getRules(
            @Query("purchase_key") String purchase_key);

}
