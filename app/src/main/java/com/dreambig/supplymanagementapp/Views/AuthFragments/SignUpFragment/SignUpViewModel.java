package com.dreambig.supplymanagementapp.Views.AuthFragments.SignUpFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        mCheckAccount = authRepo.checkEmailExistence(email);
        return mCheckAccount;
    }
}
