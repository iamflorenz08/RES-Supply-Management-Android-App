package com.dreambig.supplymanagementapp.Adapters;

import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.ItemNotificationBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notifications = new ArrayList<>();
    private NotificationListener notificationListener;
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime odt = null;
        if(!notifications.get(position).getRead()){
            holder.binding.clNotificationBG.setBackgroundColor(Color.parseColor("#C9E0FE"));
        }
        else{
            holder.binding.clNotificationBG.setBackgroundColor(Color.WHITE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            odt = OffsetDateTime.parse(notifications.get(position).getCreatedAt());
            try {
                Date date = inputFormat.parse(notifications.get(position).getCreatedAt());
                if(notifications.get(position).getApproval().equals("to_be_approved")){
                    Glide.with(holder.binding.getRoot()).load(R.drawable.tba_icon_active).into(holder.binding.ivIcon);
                    holder.binding.tvMessage.setText("Your request has been submitted.");
                    holder.binding.tvTime.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
                }
                else if(notifications.get(position).getApproval().equals("on_going")){
                    Glide.with(holder.binding.getRoot()).load(R.drawable.check_icon_active).into(holder.binding.ivIcon);
                    holder.binding.tvMessage.setText("The Admin approved your " +  odt.getMonth() + " " + odt.getDayOfMonth() + ", " + odt.getYear() + " request. ");
                    holder.binding.tvTime.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS));
                }
                else if(notifications.get(position).getApproval().equals("rejected")){
                    Glide.with(holder.binding.getRoot()).load(R.drawable.x_icon_active).into(holder.binding.ivIcon);
                    holder.binding.tvMessage.setText("The Admin rejected your " +  odt.getMonth() + " " + odt.getDayOfMonth() + ", " + odt.getYear() + " request. ");
                    holder.binding.tvTime.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS));
                }
                else if(notifications.get(position).getApproval().equals("completed")){
                    Glide.with(holder.binding.getRoot()).load(R.drawable.completed_icon_active).into(holder.binding.ivIcon);
                    holder.binding.tvMessage.setText("Your " +  odt.getMonth() + " " + odt.getDayOfMonth() + ", " + odt.getYear() + " request has been Completed. ");
                    holder.binding.tvTime.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS));
                }

                holder.binding.clNotificationBG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notificationListener.openNotification(notifications.get(position));
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void setNotificationListener(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemNotificationBinding binding;
        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface NotificationListener{
        void openNotification(NotificationModel notificationModel);
    }
}
