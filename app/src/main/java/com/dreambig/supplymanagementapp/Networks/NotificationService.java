package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationService {
    @POST("/notification")
    Call<List<NotificationModel>> loadNotifications(@Query("user") String user_id);

    @POST("/notification/read")
    Call<ResponseModel> readNotification(@Query("notif_id") String id);
}
