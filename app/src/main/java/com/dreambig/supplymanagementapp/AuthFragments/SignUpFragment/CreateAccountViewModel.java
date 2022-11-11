package com.dreambig.supplymanagementapp.AuthFragments.SignUpFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.AuthFragments.AuthViewModel;
import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateAccountViewModel extends ViewModel {
    private AuthRepo authRepo;
    private MutableLiveData<CheckAccountModel> mCheckAccount;

    @Inject
    public CreateAccountViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public LiveData<CheckAccountModel> getmCheckAccount(String email) {
        mCheckAccount = authRepo.checkEmailExistence(email);
        return mCheckAccount;
    }
}
