package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Models.RequestModel;
import com.dreambig.supplymanagementapp.Models.ResetPasswordModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("/auth/create")
    Call<String> createUser(@Body UserModel user);

    @GET("/auth/check")
    Call<AuthResponseModel> checkAccount(@Query("email") String email);

    @POST("/auth/signin")
    Call<String> signIn(@Body SignInModel credentials);

    @POST("/auth/recover")
    Call<ResponseModel> sendEmail(@Body UserModel userModel);

    @POST("/auth/reset")
    Call<ResponseModel> resetPassword(@Header("Authorization") String token, @Body ResetPasswordModel newPassword);

    @POST("/user")
    Call<UserModel> getUserInfo(@Header("Authorization")String authHeader);


}
