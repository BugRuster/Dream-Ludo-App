package com.definiteautomation.dreamludo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.activity.MatchDetailActivity;
import com.definiteautomation.dreamludo.adapter.OngoingAdapter;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.MatchModel;

import java.util.List;

public class OngoingFragment extends Fragment {

    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public TextView noDataTv;

    private ProgressBar progressBar;
    private ApiCalling api;
    private OngoingAdapter ongoingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(getActivity(), false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noDataTv = view.findViewById(R.id.noDataTv);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(Function.checkNetworkConnection(requireActivity()))
                getMatchOngoing();
        });

        swipeRefreshLayout.post(() -> {
            if(Function.checkNetworkConnection(requireActivity())) {
                getMatchOngoing();
            }
        });

        return view;
    }

    private void getMatchOngoing() {
        progressBar.showProgressDialog();

        Call<List<MatchModel>> call = api.getMatchOngoing(AppConstant.PURCHASE_KEY, Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
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
                        ongoingAdapter = new OngoingAdapter(getActivity(), legalData);
                        swipeRefreshLayout.setRefreshing(false);

                        if (ongoingAdapter.getItemCount() != 0) {
                            ongoingAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(ongoingAdapter);

                            recyclerView.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }

                        ongoingAdapter.setOnItemClickListener((view, obj, pos) -> {
                            if (Preferences.getInstance(getContext()).getString(Preferences.KEY_USER_ID).equals(obj.getParti1_id())) {
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
                                intent.putExtra("IS_JOIN_KEY", "0");
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
                                intent.putExtra("IS_JOIN_KEY", "0");
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

    @Override
    public void onResume() {
        super.onResume();
        if(Function.checkNetworkConnection(requireActivity())) {
            getMatchOngoing();
        }
    }
}