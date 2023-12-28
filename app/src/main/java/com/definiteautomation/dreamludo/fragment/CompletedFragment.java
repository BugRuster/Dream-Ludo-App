package com.definiteautomation.dreamludo.fragment;

import android.annotation.SuppressLint;
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
import com.definiteautomation.dreamludo.adapter.CompletedAdapter;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.MatchModel;

import java.util.List;


public class CompletedFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noDataTv;

    private ProgressBar progressBar;
    private ApiCalling api;
    private CompletedAdapter completedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
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
            if(Function.checkNetworkConnection(requireActivity())) {
                getMatchCompleted();
            }
        });

        swipeRefreshLayout.post(() -> {
            if(Function.checkNetworkConnection(requireActivity())) {
                getMatchCompleted();
            }
        });

        return view;
    }

    private void getMatchCompleted() {
        progressBar.showProgressDialog();

        Call<List<MatchModel>> call = api.getMatchCompleted(AppConstant.PURCHASE_KEY, Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
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
                        completedAdapter = new CompletedAdapter(getActivity(), legalData);
                        swipeRefreshLayout.setRefreshing(false);

                        if (completedAdapter.getItemCount() != 0) {
                            completedAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(completedAdapter);

                            recyclerView.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }
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
            getMatchCompleted();
        }
    }
}