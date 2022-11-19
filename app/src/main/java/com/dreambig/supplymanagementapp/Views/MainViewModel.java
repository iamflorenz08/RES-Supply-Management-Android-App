package com.dreambig.supplymanagementapp.Views;

import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private AuthRepo authRepo;

    @Inject
    public MainViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public void loadUserInfo(){
        authRepo.loadUserInfo();
    }
}