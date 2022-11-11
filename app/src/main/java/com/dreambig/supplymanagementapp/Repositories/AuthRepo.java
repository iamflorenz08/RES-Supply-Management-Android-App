package com.dreambig.supplymanagementapp.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.AuthService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepo {
    private static AuthRepo instance;
    private AuthService authService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static AuthRepo getInstance(AuthService auth, Application application){
        if(instance == null){
            instance = new AuthRepo(auth, application);
        }
        return instance;
    }

    private AuthRepo(AuthService authService, Application application){
        this.authService = authService;
        sharedPreferences = application.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public MutableLiveData<CheckAccountModel> checkEmailExistence(String email){
        final MutableLiveData<CheckAccountModel> result = new MutableLiveData<>();
        authService.checkAccount(email).enqueue(new Callback<CheckAccountModel>() {
            @Override
            public void onResponse(Call<CheckAccountModel> call, Response<CheckAccountModel> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<CheckAccountModel> call, Throwable t) {
                Log.e("MY_DEV", "getProdList onFailure" + call.toString());
            }
        });
        return result;
    }

    public MutableLiveData<Boolean> insertUser(UserModel user){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        authService.createUser(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    saveToken(response.body());
                    result.setValue(true);
                    return;
                }
                result.setValue(false);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                result.setValue(false);
            }
        });

        return result;
    }

    public MutableLiveData<Boolean> signIn(SignInModel credentials){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        authService.signIn(credentials).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    saveToken(response.body());
                    result.setValue(true);
                    return;
                }
                result.setValue(false);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(false);
            }
        });
        return result;
    }

    public void saveToken(String token){
        editor.putString("TOKEN", token);
        editor.commit();
    }

    public String getToken(){
        Log.d("TOKEN", sharedPreferences.getString("TOKEN", ""));
        return sharedPreferences.getString("TOKEN", "");
    }

    public void removeToken(){
        editor.putString("TOKEN", null);
    }


}
