package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.MatchModel;
import com.definiteautomation.dreamludo.view.VerticalTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {

    public Context context;
    private final List<MatchModel> dataArrayList;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public UpcomingAdapter(Context applicationContext, List<MatchModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    private long mMinutes = 0;
    private long mSeconds = 0;
    private long mMilliSeconds = 0;

    public TimerListener mListener;
    private CountDownTimer mCountDownTimer;

    public interface TimerListener {
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    public String startTime, currentTime;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_upcoming, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!dataArrayList.get(position).getParti1_name().equals("0") && !dataArrayList.get(position).getParti2_name().equals("0")) {
            holder.titleTv.setText(String.format("%s Vs %s", dataArrayList.get(position).getParti1_name(), dataArrayList.get(position).getParti2_name()));
        }
        else if (!dataArrayList.get(position).getParti1_name().equals("0") && dataArrayList.get(position).getParti2_name().equals("0") && dataArrayList.get(position).getType() == 1) {
            holder.titleTv.setText(String.format("%s Vs Team 2", dataArrayList.get(position).getParti1_name()));
        }
        else if (!dataArrayList.get(position).getParti1_name().equals("0") && dataArrayList.get(position).getParti2_name().equals("0") && dataArrayList.get(position).getType() == 0) {
            holder.titleTv.setText(String.format("%s Vs Player 2", dataArrayList.get(position).getParti1_name()));
        }
        else if (dataArrayList.get(position).getType() == 1) {
            holder.titleTv.setText("Team 1 Vs Team 2");
        }
        else if (dataArrayList.get(position).getType() == 0) {
            holder.titleTv.setText("Player 1 Vs Player 2");
        }

        holder.feesTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getMatch_fee()));
        holder.prizeTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getPrize()));

        holder.progressBar.setMax(dataArrayList.get(position).getTable_size());
        holder.progressBar.setProgress(dataArrayList.get(position).getTable_joined());

        try {
            currentTime = dataArrayList.get(position).getCurrent_time();
            startTime = dataArrayList.get(position).getPlay_time();

            try {
                if (Integer.parseInt(currentTime) >= Integer.parseInt(startTime)) {
                    holder.timerTv.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.timerTv.setVisibility(View.VISIBLE);
                    int time = Integer.parseInt(startTime) - Integer.parseInt(currentTime);
                    setTime(time * 1000L, holder.timerTv, holder.joinBt, holder.roomSizeTv, holder.roomStatusTv, holder.progressBar, position);
                    startCountDown();
                }
            }
            catch (NumberFormatException e) {
                holder.timerTv.setVisibility(View.INVISIBLE);
            }
        }
        catch (NullPointerException e){
            holder.timerTv.setVisibility(View.INVISIBLE);
        }

        if (dataArrayList.get(position).getType() != 0) {
            holder.typeTv.setText("Team");

            if (dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                holder.roomStatusTv.setText("No Team Left! Match is Full.");
                holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));

                holder.joinBt.setText("MATCH FULL");
                holder.joinBt.setTextColor(Color.parseColor("#ffffff"));
                holder.joinBt.setBackgroundResource(R.drawable.button_background_black);
            } else {
                int leftSize = dataArrayList.get(position).getTable_size() - dataArrayList.get(position).getTable_joined();
                holder.roomStatusTv.setText("Only "+leftSize+" Team left");
                holder.roomSizeTv.setText(String.format("Team: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
            }
        }
        else {
            holder.typeTv.setText("Single");
            if (dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                holder.roomStatusTv.setText("No Player Left! Match is Full.");
                holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));

                holder.joinBt.setText("MATCH FULL");
                holder.joinBt.setTextColor(Color.parseColor("#ffffff"));
                holder.joinBt.setBackgroundResource(R.drawable.button_background_black);
            } else {
                int leftSize = dataArrayList.get(position).getTable_size() - dataArrayList.get(position).getTable_joined();
                holder.roomStatusTv.setText("Only "+leftSize+" Player left");
                holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
            }
        }
        
        try {
            if (dataArrayList.get(position).getIs_joined() == 0 && dataArrayList.get(position).getTable_joined() < dataArrayList.get(position).getTable_size()) {
                holder.joinBt.setText("JOIN");
                holder.joinBt.setClickable(true);
                holder.joinBt.setEnabled(true);
            } else if (dataArrayList.get(position).getIs_joined() == 1) {
                holder.joinBt.setText("NEXT");
                holder.joinBt.setEnabled(true);
                holder.joinBt.setClickable(true);
            } else {
                if (dataArrayList.get(position).getType() != 0) {
                    holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                    holder.roomStatusTv.setText("No Team Left! Match is Full.");
                }
                else {
                    holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                    holder.roomStatusTv.setText("No Player Left! Match is Full.");
                }

                holder.joinBt.setText("MATCH FULL");
                holder.joinBt.setEnabled(false);
                holder.joinBt.setClickable(false);
                holder.joinBt.setTextColor(Color.parseColor("#ffffff"));
                holder.joinBt.setBackgroundResource(R.drawable.button_background_black);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        holder.joinBt.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, dataArrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, MatchModel obj, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView prizeTv;
        TextView timerTv;
        TextView feesTv;
        TextView winnerTv;
        TextView roomSizeTv;
        TextView roomStatusTv;
        Button joinBt;
        VerticalTextView typeTv;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
            timerTv = itemView.findViewById(R.id.timerTv);
            feesTv = itemView.findViewById(R.id.feesTv);
            winnerTv = itemView.findViewById(R.id.winnerTv);
            roomSizeTv = itemView.findViewById(R.id.roomSizeTv);
            roomStatusTv = itemView.findViewById(R.id.roomStatusTv);
            joinBt = itemView.findViewById(R.id.joinBt);
            typeTv = itemView.findViewById(R.id.typeTv);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    private void initCounter(TextView timeText, Button joinBt, TextView roomSizeTv, TextView roomStatusTv, ProgressBar progressBar, int position) {
        mCountDownTimer = new CountDownTimer(mMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                calculateTime(millisUntilFinished, timeText, joinBt, roomSizeTv, roomStatusTv, progressBar, position);
                if (mListener != null) {
                    mListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                calculateTime(0, timeText, joinBt, roomSizeTv, roomStatusTv, progressBar, position);
                if (mListener != null) {
                    mListener.onFinish();
                }
            }
        };
    }

    public void startCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    public void setTime(long milliSeconds, TextView timerTv, Button joinBt, TextView roomSizeTv, TextView roomStatusTv, ProgressBar progressBar, int position) {
        mMilliSeconds = milliSeconds;
        initCounter(timerTv, joinBt, roomSizeTv, roomStatusTv, progressBar, position);
        calculateTime(milliSeconds,timerTv, joinBt, roomSizeTv, roomStatusTv, progressBar, position);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateTime(long milliSeconds, TextView timerTv, Button joinBt, TextView roomSizeTv, TextView roomStatusTv, ProgressBar progressBar, int position) {
        if (milliSeconds != 0) {
            mSeconds = (milliSeconds / 1000) % 60;
            mMinutes = (milliSeconds / (1000 * 60)) % 60;

            displayText(timerTv);
        }
        else {
            timerTv.setVisibility(View.INVISIBLE);
            joinBt.setText("JOIN");
            joinBt.setClickable(true);
            joinBt.setEnabled(true);

            progressBar.setProgress(0);
            if (dataArrayList.get(position).getType() != 0) {
                roomStatusTv.setText(String.format("Only %d Team left", dataArrayList.get(position).getTable_size()));
                roomSizeTv.setText(String.format("Team: 0/%d", dataArrayList.get(position).getTable_size()));
            }
            else {
                roomStatusTv.setText(String.format("Only %d Player left", dataArrayList.get(position).getTable_size()));
                roomSizeTv.setText(String.format("Player: 0/%d", dataArrayList.get(position).getTable_size()));
            }
        }
    }

    private void displayText(TextView timeText) {
        try{
            String stringBuilder = "Board close in\n" + getTwoDigitNumber(mMinutes) + "m : " + getTwoDigitNumber(mSeconds) + "s";
            timeText.setText(stringBuilder);
        }catch (NullPointerException e){
            timeText.setVisibility(View.INVISIBLE);
        }
    }

    private String getTwoDigitNumber(long number) {
        if (number >= 0 && number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
}

