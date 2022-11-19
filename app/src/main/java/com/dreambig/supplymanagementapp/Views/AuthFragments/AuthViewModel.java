package com.dreambig.supplymanagementapp.Views.AuthFragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel{


    private LiveData<Boolean> isSignedIn;
    private LiveData<AuthResponseModel> mCheckGoogleAccount;
    private AuthRepo authRepo;

    @Inject
    public AuthViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public void init(){
        if (isSignedIn == null)
            isSignedIn = authRepo.getIsSignedIn();

        if (mCheckGoogleAccount == null)
            mCheckGoogleAccount = authRepo.getmCheckGoogleAccount();
    }

    public void checkAutoSignIn(){
        authRepo.checkAutoSignIn();
    }

    public LiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }

    public void checkGoogleAccount(String email){
        authRepo.checkGoogleAccount(email);
    }

    public LiveData<AuthResponseModel> getmCheckGoogleAccount() {
        return mCheckGoogleAccount;
    }

    public void signOut(){
        authRepo.removeToken();
    }

    public void updateToken(String idToken) {
        authRepo.saveToken(idToken);
    }
}
