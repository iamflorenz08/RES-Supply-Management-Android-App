package com.dreambig.supplymanagementapp.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Models.RequestModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.AuthService;
import com.dreambig.supplymanagementapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepo {
    private static AuthRepo instance;
    private AuthService authService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Application application;
    private MutableLiveData<Boolean> isSignedIn;
    private MutableLiveData<AuthResponseModel> mCheckGoogleAccount;
    private MutableLiveData<AuthResponseModel> mCheckEmail;
    private MutableLiveData<AuthResponseModel> mSignUpGmail;
    private MutableLiveData<UserModel> authenticatedUser;

    public static AuthRepo getInstance(AuthService auth, Application application){
        if(instance == null){
            instance = new AuthRepo(auth, application);
        }
        return instance;
    }

    private AuthRepo(AuthService authService, Application application){
        this.authService = authService;
        this.application = application;
        sharedPreferences = application.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isSignedIn = new MutableLiveData<>();
        mCheckGoogleAccount = new MutableLiveData<>();
        mCheckEmail = new MutableLiveData<>();
        mSignUpGmail = new MutableLiveData<>();
        authenticatedUser = new MutableLiveData<>();
    }

    public void checkGoogleAccount(String email){
        authService.checkAccount(email).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                if(response.isSuccessful()){
                    mCheckGoogleAccount.postValue(response.body());
                }
                else{
                    mCheckGoogleAccount.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                mCheckGoogleAccount.postValue(null);
            }
        });
    }

    public void checkSignUpGoogle(String email){
        authService.checkAccount(email).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                if(response.isSuccessful()){
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(application.getApplicationContext());
                    saveToken(account.getIdToken());
                    mSignUpGmail.postValue(response.body());
                }
                else {
                    mSignUpGmail.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                mSignUpGmail.postValue(null);
            }
        });
    }

    public void checkEmail(String email){
        authService.checkAccount(email).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                if(response.isSuccessful()){
                    mCheckEmail.postValue(response.body());
                }
                else{
                    mCheckEmail.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                mCheckEmail.postValue(null);
            }
        });
    }


    public void checkAutoSignIn(){
        if(getToken().trim().isEmpty() || getToken() == null)
            isSignedIn.setValue(false);
        else
            isSignedIn.setValue(true);
    }

    public void loadUserInfo(){
        String header = "Bearer " + getToken();
        authService.getUserInfo(header).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful())
                    authenticatedUser.postValue(response.body());
                else
                    authenticatedUser.postValue(null);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                authenticatedUser.postValue(null);
            }
        });
    }


    public MutableLiveData<Boolean> insertUser(UserModel user){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        authService.createUser(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(application.getApplicationContext());
                    if(account != null)
                        saveToken(account.getIdToken());
                    else
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
        authenticatedUser.setValue(null);
        editor.putString("TOKEN", null);
        editor.commit();
    }

    public LiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }

    public LiveData<AuthResponseModel> getmCheckGoogleAccount() {
        return mCheckGoogleAccount;
    }

    public LiveData<AuthResponseModel> getmCheckEmail() {
        mCheckEmail.setValue(null);
        return mCheckEmail;
    }

    public LiveData<AuthResponseModel> getmSignUpGmail() {
        mSignUpGmail.setValue(null);
        return mSignUpGmail;
    }

    public LiveData<UserModel> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void sendEmail(String email) {
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        authService.sendEmail(userModel).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
}
