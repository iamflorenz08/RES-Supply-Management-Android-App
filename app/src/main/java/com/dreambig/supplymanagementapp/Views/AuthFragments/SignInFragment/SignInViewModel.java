package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.PasswordModel;
import com.dreambig.supplymanagementapp.Models.ResetPasswordModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private AuthRepo authRepo;
    private MutableLiveData<Boolean> isLoginSuccess;
    private MutableLiveData<ResponseModel> responseResetPassword;

    @Inject
    public SignInViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public void init(){
        if(responseResetPassword == null)
            responseResetPassword = authRepo.getResponseResetPassword();
    }

    public MutableLiveData<Boolean> getIsLoginSuccess(SignInModel credentials) {
        isLoginSuccess = authRepo.signIn(credentials);
        return isLoginSuccess;
    }

    public void sendEmail(String email) {
        authRepo.sendEmail(email);
    }

    public void resetPassword(String token, String newPassword) {
        authRepo.resetPassword(token, newPassword);
    }

    public LiveData<ResponseModel> getResponseResetPassword() {
        return responseResetPassword;
    }
}
