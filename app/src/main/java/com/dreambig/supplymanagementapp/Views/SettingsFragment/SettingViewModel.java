package com.dreambig.supplymanagementapp.Views.SettingsFragment;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.ProfileService;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.FirebaseStorageRepo;
import com.dreambig.supplymanagementapp.Repositories.ProfileRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private AuthRepo authRepo;
    private FirebaseStorageRepo firebaseStorageRepo;
    private ProfileRepo profileRepo;
    private LiveData<UserModel> user;
    private MutableLiveData<String> mProfileUri;
    private MutableLiveData<Boolean> isUpdatePhotoSuccess;

    @Inject
    public SettingViewModel(AuthRepo authRepo, FirebaseStorageRepo firebaseStorageRepo, ProfileRepo profileRepo){
        this.authRepo = authRepo;
        this.firebaseStorageRepo = firebaseStorageRepo;
        this.profileRepo = profileRepo;
    }

    public void init(){
        if(user == null)
            user = authRepo.getAuthenticatedUser();

        if (mProfileUri == null)
            mProfileUri = firebaseStorageRepo.getmProfileUri();

        if(isUpdatePhotoSuccess == null)
            isUpdatePhotoSuccess = profileRepo.getIsUpdatePhotoSuccess();
    }

    public LiveData<UserModel> getUser() {
        return user;
    }

    public void loadUserInfo(){
        authRepo.loadUserInfo();
    }

    public void uploadProfilePhoto(Uri uri){
        firebaseStorageRepo.uploadProfilePhoto(uri);
    }

    public LiveData<String> getmProfileUri() {
        mProfileUri.setValue(null);
        return mProfileUri;
    }

    public void updatePhoto(String photo_URL){
        profileRepo.updatePhoto(photo_URL);
    }

    public LiveData<Boolean> getIsUpdatePhotoSuccess() {
        isUpdatePhotoSuccess.setValue(null);
        return isUpdatePhotoSuccess;
    }

    public void deletePhoto(String fileName){
        firebaseStorageRepo.deletePhoto(fileName);
    }


}