package com.dreambig.supplymanagementapp.Views.AuthFragments.SignUpFragment;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SetUpViewModel extends ViewModel {
    private AuthRepo authRepo;
    private MutableLiveData<Boolean> isCreateSuccess;

    @Inject
    public SetUpViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }


    public LiveData<Boolean> getIsCreateSuccess(UserModel user){
        if(isCreateSuccess == null)
            isCreateSuccess = authRepo.insertUser(user);;
        return isCreateSuccess;
    }

}
