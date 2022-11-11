package com.dreambig.supplymanagementapp.AuthFragments.SignUpFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.AuthFragments.AuthViewModel;
import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private AuthRepo authRepo ;
    private MutableLiveData<CheckAccountModel> mCheckAccount;

    @Inject
    public SignUpViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public MutableLiveData<CheckAccountModel> getmCheckAccount(String email) {
        if(mCheckAccount == null)
            mCheckAccount = authRepo.checkEmailExistence(email);
        return mCheckAccount;
    }
}
