package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.HistoryModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<HistoryModel> dataArrayList;

    public HistoryAdapter(Context applicationContext, List<HistoryModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_transactions, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (dataArrayList.get(position).getType().equals("0")) {
            holder.amountTv.setText(String.format("- %s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getAmount()));
            holder.amountTv.setTextColor(context.getResources().getColor(R.color.colorError));
        }
        else if (dataArrayList.get(position).getType().equals("1")) {
            holder.amountTv.setText(String.format("+ %s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getAmount()));
            holder.amountTv.setTextColor(context.getResources().getColor(R.color.colorSuccess));
        }

        switch (dataArrayList.get(position).getStatus()) {
            case "0":
                holder.statusTv.setText("Payment Pending");
                holder.statusIv.setImageResource(R.drawable.ic_pending);
                break;
            case "1":
                holder.statusTv.setText("Payment Completed");
                holder.statusIv.setImageResource(R.drawable.ic_accept);
                break;
            case "2":
                holder.statusTv.setText("Payment Rejected");
                holder.statusIv.setImageResource(R.drawable.ic_reject);
                break;
        }

        holder.remarkTv.setText(dataArrayList.get(position).getRemark());
        holder.timeTv.setText(dataArrayList.get(position).getDate_created());

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView statusTv;
        TextView remarkTv;
        TextView timeTv;
        TextView amountTv;
        ImageView statusIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTv = itemView.findViewById(R.id.statusTv);
            remarkTv = itemView.findViewById(R.id.remarkTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            statusIv = itemView.findViewById(R.id.statusIv);
        }
    }

}


