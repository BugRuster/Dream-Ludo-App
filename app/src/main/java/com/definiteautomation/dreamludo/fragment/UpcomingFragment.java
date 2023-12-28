package com.definiteautomation.dreamludo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.activity.DepositActivity;
import com.definiteautomation.dreamludo.activity.LoginActivity;
import com.definiteautomation.dreamludo.activity.MainActivity;
import com.definiteautomation.dreamludo.activity.MatchDetailActivity;
import com.definiteautomation.dreamludo.adapter.UpcomingAdapter;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.MatchModel;
import com.definiteautomation.dreamludo.model.MyResponse;
import com.definiteautomation.dreamludo.model.Notification;
import com.definiteautomation.dreamludo.model.Sender;
import com.definiteautomation.dreamludo.model.UserModel;
import com.definiteautomation.dreamludo.remote.APIService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UpcomingFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noDataTv;

    private ProgressBar progressBar;
    private ApiCalling api;
    private UpcomingAdapter upcomingAdapter;

    private double deposit = 0, winning = 0, bonus = 0, total = 0;
    private DatabaseReference mUserRef;

    private int flag = 0;
    private String token;
    public APIService mService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(getActivity(), false);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.recyclerView);
        noDataTv = view.findViewById(R.id.noDataTv);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(Function.checkNetworkConnection(requireActivity())) {
                getMatchUpcoming();
            }
        });

        swipeRefreshLayout.post(() -> {
            if(Function.checkNetworkConnection(requireActivity())) {
                getMatchUpcoming();
            }
        });

        if(Function.checkNetworkConnection(requireActivity())) {
            getUserDetails();
        }

        return view;
    }

    private void getMatchUpcoming() {
        progressBar.showProgressDialog();

        Call<List<MatchModel>> call = api.getMatchUpcoming(AppConstant.PURCHASE_KEY, Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
        call.enqueue(new Callback<List<MatchModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<MatchModel>> call, @NonNull Response<List<MatchModel>> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    List<MatchModel> legalData = response.body();
                    if (legalData != null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        upcomingAdapter = new UpcomingAdapter(getActivity(), legalData);
                        swipeRefreshLayout.setRefreshing(false);

                        if (upcomingAdapter.getItemCount() != 0) {
                            upcomingAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(upcomingAdapter);

                            recyclerView.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }

                        upcomingAdapter.setOnItemClickListener((view, obj, pos) -> {
                            if (obj.getIs_joined() == 0 && obj.getTable_joined() < obj.getTable_size()) {
                                AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                                        // set dialog icon
                                        .setIcon(R.drawable.ic_game)
                                        // Set Dialog Title
                                        .setTitle("JOIN MATCH")
                                        // Set Dialog Message
                                        .setMessage("Are you sure you want to join?")
                                        // positive button
                                        .setPositiveButton("Confirm", (dialog, which) -> {
                                            if (total >= obj.getMatch_fee()) {
                                                joinMatch(obj);
                                            } else {
                                                startActivity(new Intent(getActivity(), DepositActivity.class));
                                                Function.showToast(getActivity(), "No enough balance to join match.");
                                            }
                                        })
                                        // negative button
                                        .setNegativeButton("Cancel", (dialog, which) -> {
                                        }).create();

                                alertDialog.show();
                            } else if (Preferences.getInstance(getContext()).getString(Preferences.KEY_USER_ID).equals(obj.getParti1_id())) {
                                Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                                intent.putExtra("ID_KEY", obj.getId());
                                intent.putExtra("FEE_KEY", obj.getMatch_fee());
                                intent.putExtra("PRIZE_KEY", obj.getPrize());
                                intent.putExtra("TYPE_KEY", obj.getType());
                                intent.putExtra("CURR_TIME_KEY", obj.getCurrent_time());
                                intent.putExtra("PLAY_TIME_KEY", obj.getPlay_time());
                                intent.putExtra("PARTI1_ID_KEY", obj.getParti1_id());
                                intent.putExtra("PARTI2_ID_KEY", obj.getParti2_id());
                                intent.putExtra("PARTI1_NAME_KEY", obj.getParti1_name());
                                intent.putExtra("PARTI2_NAME_KEY", obj.getParti2_name());
                                intent.putExtra("WHATSAPP_KEY", obj.getWhatsapp_no2());
                                startActivity(intent);
                            }
                            else if (Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID).equals(obj.getParti2_id())) {
                                Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                                intent.putExtra("ID_KEY", obj.getId());
                                intent.putExtra("FEE_KEY", obj.getMatch_fee());
                                intent.putExtra("PRIZE_KEY", obj.getPrize());
                                intent.putExtra("TYPE_KEY", obj.getType());
                                intent.putExtra("CURR_TIME_KEY", obj.getCurrent_time());
                                intent.putExtra("PLAY_TIME_KEY", obj.getPlay_time());
                                intent.putExtra("PARTI1_ID_KEY", obj.getParti1_id());
                                intent.putExtra("PARTI2_ID_KEY", obj.getParti2_id());
                                intent.putExtra("PARTI1_NAME_KEY", obj.getParti1_name());
                                intent.putExtra("PARTI2_NAME_KEY", obj.getParti2_name());
                                intent.putExtra("WHATSAPP_KEY", obj.getWhatsapp_no1());
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                        noDataTv.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    noDataTv.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchModel>> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void getUserDetails() {
        Call<UserModel> call = api.getUserDetails(AppConstant.PURCHASE_KEY, Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            deposit = res.get(0).getDeposit_bal();
                            winning = res.get(0).getWon_bal();
                            bonus = res.get(0).getBonus_bal();
                            total = deposit + winning + bonus;

                            if (res.get(0).getIs_block() == 1) {
                                Preferences.getInstance(getActivity()).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            else if (res.get(0).getIs_active() == 0) {
                                Preferences.getInstance(getActivity()).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void joinMatch(MatchModel obj) {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Check if already user is exist
                if (!dataSnapshot.child(Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID)).exists()) {
                    registerUser(Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
                }
                else {
                    FirebaseMessaging.getInstance ().getToken ().addOnCompleteListener ( task -> {
                                if (!task.isSuccessful ()) {
                                    //Could not get FirebaseMessagingToken
                                    return;
                                }
                                if (null != task.getResult ()) {
                                    //Got FirebaseMessagingToken
                                    String device_token = Objects.requireNonNull ( task.getResult () );
                                    //Use firebaseMessagingToken further
                                    DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                    mUserDatabase.child(Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID)).child("device_token").setValue(device_token).addOnSuccessListener(aVoid -> { });
                                }
                            } );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (obj.getTable_joined() == 0) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.postParticipant1Result(AppConstant.PURCHASE_KEY, obj.getId(), Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
            call.enqueue(new Callback<MatchModel>() {
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                Function.showToast(getActivity(), res.get(0).getMsg());

                                Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                                intent.putExtra("ID_KEY", obj.getId());
                                intent.putExtra("FEE_KEY", obj.getMatch_fee());
                                intent.putExtra("PRIZE_KEY", obj.getPrize());
                                intent.putExtra("TYPE_KEY", obj.getType());
                                intent.putExtra("CURR_TIME_KEY", res.get(0).getCurrent_time());
                                intent.putExtra("PLAY_TIME_KEY", res.get(0).getPlay_time());
                                intent.putExtra("PARTI1_ID_KEY", obj.getParti1_id());
                                intent.putExtra("PARTI2_ID_KEY", obj.getParti2_id());
                                intent.putExtra("PARTI1_NAME_KEY", obj.getParti1_name());
                                intent.putExtra("PARTI2_NAME_KEY", obj.getParti2_name());
                                intent.putExtra("WHATSAPP_KEY", obj.getWhatsapp_no2());
                                intent.putExtra("IS_JOIN_KEY", "1");
                                startActivity(intent);
                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(getActivity(), res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                }
            });
        }
        else if (obj.getTable_joined() == 1) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.postParticipant2Result(AppConstant.PURCHASE_KEY, obj.getId(), Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
            call.enqueue(new Callback<MatchModel>() {
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                Function.showToast(getActivity(), res.get(0).getMsg());

                                getUserToken(obj.getParti1_id(), Preferences.getInstance(getActivity()).getString(Preferences.KEY_FULL_NAME));

                                Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                                intent.putExtra("ID_KEY", obj.getId());
                                intent.putExtra("FEE_KEY", obj.getMatch_fee());
                                intent.putExtra("PRIZE_KEY", obj.getPrize());
                                intent.putExtra("TYPE_KEY", obj.getType());
                                intent.putExtra("CURR_TIME_KEY", res.get(0).getCurrent_time());
                                intent.putExtra("PLAY_TIME_KEY", obj.getPlay_time());
                                intent.putExtra("PARTI1_ID_KEY", obj.getParti1_id());
                                intent.putExtra("PARTI2_ID_KEY", obj.getParti2_id());
                                intent.putExtra("PARTI1_NAME_KEY", obj.getParti1_name());
                                intent.putExtra("PARTI2_NAME_KEY", obj.getParti2_name());
                                intent.putExtra("WHATSAPP_KEY", obj.getWhatsapp_no1());
                                intent.putExtra("IS_JOIN_KEY", "1");
                                startActivity(intent);

                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(getActivity(), res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                }
            });
        }
    }

    private void getUserToken(String id, String name) {
        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                token = Objects.requireNonNull(dataSnapshot.child("device_token").getValue()).toString();

                if (flag == 0) {
                    flag = 1;
                    sendNotification(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String name) {
        mService = AppConstant.getFCMService();

        Map<String,String> map = new HashMap<>();
        map.put("title","Challenge Accepted");
        map.put("message",name + " has accepted your challenge. Update your room code to play.");
        map.put("click_action","MainActivity");

        //Create raw payload  to send
        Notification notification = new Notification("Challenge Accepted",name + " has accepted your challenge. Update your room code to play.","MainActivity");
        Sender content =  new Sender(token,notification);

        mService.sendNotification(content)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                        //Only run when get result
                    }

                    @Override
                    public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                        Log.e("ERROR", Objects.requireNonNull(t.getMessage()));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Function.checkNetworkConnection(requireActivity())) {
            getMatchUpcoming();
            getUserDetails();
        }
    }

    private void registerUser(String id) {
        FirebaseMessaging.getInstance ().getToken ().addOnCompleteListener ( task -> {
            if (!task.isSuccessful ()) {
                //Could not get FirebaseMessagingToken
                return;
            }
            if (null != task.getResult ()) {
                //Got FirebaseMessagingToken
                String device_token = Objects.requireNonNull ( task.getResult () );
                //Use firebaseMessagingToken further

                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("name", Preferences.getInstance(getActivity()).getString(Preferences.KEY_FULL_NAME));
                userMap.put("status", "Hi there I'm using "+getString(R.string.app_name)+" App.");
                userMap.put("image", Preferences.getInstance(getActivity()).getString(Preferences.KEY_PROFILE_PHOTO));
                userMap.put("thumb_image", Preferences.getInstance(getActivity()).getString(Preferences.KEY_PROFILE_PHOTO));
                userMap.put("device_token", device_token);

                mUserRef.child(id).setValue(userMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                });
            }
        } );
    }

}