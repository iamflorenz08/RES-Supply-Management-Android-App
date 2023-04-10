package com.dreambig.supplymanagementapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Networks.NotificationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepo {
    private NotificationService notificationService;
    private MutableLiveData<List<NotificationModel>> liveNotifications = new MutableLiveData<>();
    public NotificationRepo(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    public void loadNotifications(String user_id){
        notificationService.loadNotifications(user_id).enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if(response.isSuccessful()){
                    liveNotifications.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

            }
        });
    }

    public void readNotification(String id) {
        notificationService.readNotification(id).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    public LiveData<List<NotificationModel>> getLiveNotifications() {
        return liveNotifications;
    }


}
