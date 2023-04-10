package com.dreambig.supplymanagementapp.Views.NotificationFragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.NotificationRepo;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NotificationViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private NotificationRepo notificationRepo;
    private AuthRepo authRepo;
    private LiveData<List<NotificationModel>> liveNotifications;

    @Inject
    NotificationViewModel(NotificationRepo notificationRepo, AuthRepo authRepo){
        this.notificationRepo = notificationRepo;
        this.authRepo = authRepo;
    }

    public void init(){
        if(liveNotifications ==  null)
            liveNotifications = notificationRepo.getLiveNotifications();
    }

    public void loadNotifications(){
        notificationRepo.loadNotifications(authRepo.getAuthenticatedUser().getValue().get_id());
    }

    public void readNotification(String id) {
        notificationRepo.readNotification(id);
    }

    public LiveData<List<NotificationModel>> getLiveNotifications() {
        return liveNotifications;
    }


}