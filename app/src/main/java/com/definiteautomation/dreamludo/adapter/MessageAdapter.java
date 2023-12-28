package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.fragment.DialogFullscreenFragment;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.model.Messages;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.utils.GetTimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private final Context context;
    private final List<Messages> mMessageList;
    public DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList, Context context) {
        this.mMessageList = mMessageList;
        this.context = context;
    }

    @NotNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout ,parent, false);
        return new MessageViewHolder(v);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircularImageView profileImage;
        public TextView displayName;
        public TextView displayTime;
        public ImageView messageImage;

        public TextView messageRightText;
        public CircularImageView profileRightImage;
        public TextView displayRightName;
        public TextView displayRightTime;
        public ImageView messageRightImage;

        public MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            displayTime = view.findViewById(R.id.time_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);

            messageRightText = view.findViewById(R.id.message_text_right_layout);
            profileRightImage = view.findViewById(R.id.message_profile_right_layout);
            displayRightName = view.findViewById(R.id.name_text_right_layout);
            displayRightTime = view.findViewById(R.id.time_text_right_layout);
            messageRightImage = view.findViewById(R.id.message_image_right_layout);

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();
        long date = c.getTime();

        if (Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(from_user)) {
            viewHolder.messageText.setVisibility(View.GONE);
            viewHolder.profileImage.setVisibility(View.GONE);
            viewHolder.displayName.setVisibility(View.GONE);
            viewHolder.displayTime.setVisibility(View.GONE);
            viewHolder.messageImage.setVisibility(View.GONE);

            viewHolder.messageRightText.setVisibility(View.VISIBLE);
            viewHolder.profileRightImage.setVisibility(View.VISIBLE);
            viewHolder.displayRightName.setVisibility(View.VISIBLE);
            viewHolder.displayRightTime.setVisibility(View.VISIBLE);
            viewHolder.messageRightImage.setVisibility(View.VISIBLE);

            viewHolder.displayRightTime.setText(GetTimeAgo.getTimeAgo(date, context));

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

            try {
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                        String image = Objects.requireNonNull(dataSnapshot.child("thumb_image").getValue()).toString();

                        viewHolder.displayRightName.setText(name);
                        Glide.with(context.getApplicationContext()).load(AppConstant.API_URL+image)
                                .apply(new RequestOptions().override(120,120))
                                .apply(new RequestOptions().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .apply(RequestOptions.skipMemoryCacheOf(true))
                                .into(viewHolder.profileRightImage);
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });
            }
            catch (NullPointerException e) {
                viewHolder.displayRightName.setText("User");
                Glide.with(context.getApplicationContext()).load(AppConstant.API_URL+"default_avatar.png")
                        .apply(new RequestOptions().override(120,120))
                        .apply(new RequestOptions().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .into(viewHolder.profileRightImage);
            }

            if(message_type.equals("text")) {
                viewHolder.messageRightText.setText(c.getMessage());
                viewHolder.messageRightImage.setVisibility(View.GONE);
            } else {
                viewHolder.messageRightText.setVisibility(View.GONE);
                Glide.with(context.getApplicationContext()).load(AppConstant.API_URL+c.getMessage())
                        //.apply(new RequestOptions().override(120,120))
                        .apply(new RequestOptions().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .into(viewHolder.messageRightImage);
            }
        }
        else {
            viewHolder.messageRightText.setVisibility(View.GONE);
            viewHolder.profileRightImage.setVisibility(View.GONE);
            viewHolder.displayRightName.setVisibility(View.GONE);
            viewHolder.displayRightTime.setVisibility(View.GONE);
            viewHolder.messageRightImage.setVisibility(View.GONE);

            viewHolder.messageText.setVisibility(View.VISIBLE);
            viewHolder.profileImage.setVisibility(View.VISIBLE);
            viewHolder.displayName.setVisibility(View.VISIBLE);
            viewHolder.displayTime.setVisibility(View.VISIBLE);
            viewHolder.messageImage.setVisibility(View.VISIBLE);

            viewHolder.displayTime.setText(GetTimeAgo.getTimeAgo(date, context));

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    String image = Objects.requireNonNull(dataSnapshot.child("thumb_image").getValue()).toString();

                    viewHolder.displayName.setText(name);
                    Glide.with(context.getApplicationContext()).load(AppConstant.API_URL+image)
                            .apply(new RequestOptions().override(120,120))
                            .apply(new RequestOptions().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .into(viewHolder.profileImage);
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {

                }
            });

            if(message_type.equals("text")) {
                viewHolder.messageText.setText(c.getMessage());
                viewHolder.messageImage.setVisibility(View.GONE);
            } else {
                viewHolder.messageText.setVisibility(View.GONE);
                Glide.with(context.getApplicationContext()).load((c.getMessage()))
                        //.apply(new RequestOptions().override(120,120))
                        .apply(new RequestOptions().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .into(viewHolder.messageImage);
            }
        }

        viewHolder.messageImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("POST_KEY", c.getMessage());

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            DialogFullscreenFragment newFragment = new DialogFullscreenFragment();
            newFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });

        viewHolder.messageRightImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("POST_KEY", c.getMessage());

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            DialogFullscreenFragment newFragment = new DialogFullscreenFragment();
            newFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });

        viewHolder.itemView.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", c.getMessage());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Message Copied", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
