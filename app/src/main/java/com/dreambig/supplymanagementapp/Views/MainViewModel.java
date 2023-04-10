package com.dreambig.supplymanagementapp.Views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.NotificationRepo;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private AuthRepo authRepo;
    private NotificationRepo notificationRepo;
    private MutableLiveData<Integer> fragmentID;
    private MutableLiveData<RequisitionModel> request;
    private LiveData<UserModel> liveAuthenticatedUser;
    private LiveData<List<NotificationModel>> liveNotification;

    @Inject
    public MainViewModel(AuthRepo authRepo, NotificationRepo notificationRepo){
        this.authRepo = authRepo;
        this.notificationRepo = notificationRepo;
    }

    public void init(){
        if(fragmentID == null)
            this.fragmentID = new MutableLiveData<>();

        if(request == null)
            this.request = new MutableLiveData<>();

        if(liveAuthenticatedUser == null)
            this.liveAuthenticatedUser = authRepo.getAuthenticatedUser();

        if(liveNotification == null)
            this.liveNotification = notificationRepo.getLiveNotifications();
    }

    public void loadNotification(String user_id){
        notificationRepo.loadNotifications(user_id);
    }

    public void loadUserInfo(){
        authRepo.loadUserInfo();
    }

    public void setFragmentID(Integer fragmentID){
        this.fragmentID.setValue(fragmentID);
    }

    public void setRequest(RequisitionModel request){
        this.request.setValue(request);
    }

    public LiveData<Integer> getFragmentID() {
        return fragmentID;
    }

    public LiveData<RequisitionModel> getRequest() {
        return request;
    }

    public LiveData<UserModel> getLiveAuthenticatedUser() {
        return liveAuthenticatedUser;
    }

    public LiveData<List<NotificationModel>> getLiveNotification() {
        return liveNotification;
    }
}