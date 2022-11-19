package com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AccountSettingsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private LiveData<UserModel> mUserInfo;
    private AuthRepo authRepo;

    @Inject
    public AccountSettingsViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public void init(){
        if(mUserInfo == null)
            mUserInfo = authRepo.getAuthenticatedUser();
    }

    public LiveData<UserModel> getmUserInfo() {
        return mUserInfo;
    }
}