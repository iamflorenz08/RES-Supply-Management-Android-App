package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.RequestModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProfileService {
    @POST("/profile/update_photo")
    Call<ResponseModel> updatePhoto(@Header("Authorization")String authHeader, @Body UserModel userModel);
}
