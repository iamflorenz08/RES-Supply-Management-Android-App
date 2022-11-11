package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("/auth/create")
    Call<String> createUser(@Body UserModel user);

    @GET("/auth/check")
    Call<CheckAccountModel> checkAccount(@Query("email") String email);

    @POST("/auth/signin")
    Call<String> signIn(@Body SignInModel credentials);
}
