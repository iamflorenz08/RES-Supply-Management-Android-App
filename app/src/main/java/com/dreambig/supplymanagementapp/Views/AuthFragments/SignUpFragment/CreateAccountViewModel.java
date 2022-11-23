package com.dreambig.supplymanagementapp.Views.AuthFragments.SignUpFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateAccountViewModel extends ViewModel {
    private AuthRepo authRepo;
    private LiveData<AuthResponseModel> mCheckEmail;

    @Inject
    public CreateAccountViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public void init(){
        mCheckEmail = authRepo.getmCheckEmail();
    }

    public void checkEmail(String email){
        authRepo.checkEmail(email);
    }

    public LiveData<AuthResponseModel> getmCheckEmail() {
        return mCheckEmail;
    }
}
