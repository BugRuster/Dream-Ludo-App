package com.definiteautomation.dreamludo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.activity.NotificationDetailsActivity;
import com.definiteautomation.dreamludo.model.NotificationModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<NotificationModel> dataArrayList;

    public NotificationAdapter(Context applicationContext, List<NotificationModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(dataArrayList.get(position).getImage() != null) {
            Glide.with(context).load(dataArrayList.get(position).getImage())
                    //.apply(new RequestOptions().override(120,120))
                    .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .into(holder.iconTv);
        }

        holder.titleTV.setText(dataArrayList.get(position).getTitle());
        holder.timeTv.setText(dataArrayList.get(position).getDate_created());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationDetailsActivity.class);
            intent.putExtra("title", dataArrayList.get(position).getTitle());
            intent.putExtra("description", dataArrayList.get(position).getDescription());
            intent.putExtra("image", dataArrayList.get(position).getImage());
            intent.putExtra("url", dataArrayList.get(position).getUrl());
            intent.putExtra("created", dataArrayList.get(position).getDate_created());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconTv;
        TextView titleTV;
        TextView timeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconTv = itemView.findViewById(R.id.iconTv);
            titleTV = itemView.findViewById(R.id.titleTV);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }

}
