package com.dreambig.supplymanagementapp.Views.AuthFragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel{


    private MutableLiveData<Boolean> isSignedIn;
    private MutableLiveData<Boolean> isSignedOut;
    private MutableLiveData<CheckAccountModel> mCheckAccount;
    private AuthRepo authRepo;

    @Inject
    public AuthViewModel(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    public LiveData<Boolean> getIsSignedIn() {
        if(isSignedIn == null){
            isSignedIn = new MutableLiveData<>();
            if (!authRepo.getToken().trim().isEmpty())
                isSignedIn.setValue(true);
        }
        return isSignedIn;
    }

    public LiveData<Boolean> getIsSignedOut(){
        if(isSignedOut == null){
            isSignedOut = new MutableLiveData<>();
        }
        return isSignedOut;
    }

    public LiveData<CheckAccountModel> getmCheckAccount(String email) {
        if(mCheckAccount == null)
            mCheckAccount = authRepo.checkEmailExistence(email);
        return mCheckAccount;
    }

    public void signOut(){
        authRepo.removeToken();
    }



}
