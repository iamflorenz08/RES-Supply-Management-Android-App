package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private AuthRepo authRepo;
    private MutableLiveData<Boolean> isLoginSuccess;

    @Inject
    public SignInViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public MutableLiveData<Boolean> getIsLoginSuccess(SignInModel credentials) {
        isLoginSuccess = authRepo.signIn(credentials);
        return isLoginSuccess;
    }

    public void sendEmail(String email) {
        authRepo.sendEmail(email);
    }
}
