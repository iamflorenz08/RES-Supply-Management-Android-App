package com.dreambig.supplymanagementapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Locals.Token;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.ProfileService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepo {
    private ProfileService service;
    private MutableLiveData<Boolean> isUpdatePhotoSuccess;
    private Token tokenStorage;

    public ProfileRepo(ProfileService service, Token tokenStorage){
        this.service = service;
        this.tokenStorage = tokenStorage;
        isUpdatePhotoSuccess = new MutableLiveData<>();
    }

    public void updatePhoto(String photo_url) {
        UserModel user = new UserModel();
        user.setPhoto_URL(photo_url);

        service.updatePhoto("Bearer " + tokenStorage.readToken(),user).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    if (!response.body().getError()){
                        isUpdatePhotoSuccess.setValue(true);
                        return;
                    }
                }
                isUpdatePhotoSuccess.postValue(false);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                isUpdatePhotoSuccess.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsUpdatePhotoSuccess() {
        return isUpdatePhotoSuccess;
    }
}
