package com.dreambig.supplymanagementapp.Views.BorrowFragments.SignUpFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private AuthRepo authRepo ;
    private LiveData<AuthResponseModel> mSignUpGmail;

    @Inject
    public SignUpViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public void init(){
        mSignUpGmail = authRepo.getmSignUpGmail();
    }

    public void checkGoogleAccount(String email){
        authRepo.checkSignUpGoogle(email);
    }

    public LiveData<AuthResponseModel> getmSignUpGmail() {
        return mSignUpGmail;
    }
}
