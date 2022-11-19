package com.dreambig.supplymanagementapp.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.RequestModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.SupplyService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyRepo {
    private static SupplyRepo instance;
    private SupplyService service;
    private AuthRepo authRepo;
    private Application application;
    private MutableLiveData<ArrayList<SupplyModel>> mSupplies;

//    public static SupplyRepo getInstance(SupplyService service, AuthRepo authRepo, Application application){
//        if(instance==null){
//            instance = new SupplyRepo(service, authRepo, application);
//        }
//        return instance;
//    }

    public SupplyRepo(SupplyService service, AuthRepo authRepo, Application application){
        this.service = service;
        this.authRepo = authRepo;
        this.application = application;
        mSupplies = new MutableLiveData<>();
    }

    public void loadmSupplies(){
        String token = "Bearer " + authRepo.getToken();
        service.getDetails(token).enqueue(new Callback<ArrayList<SupplyModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SupplyModel>> call, Response<ArrayList<SupplyModel>> response) {
                if (response.isSuccessful()){
                    mSupplies.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SupplyModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    public void loadmSupplies(){
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(application.getApplicationContext());
//        String token = "Bearer ";
//        Boolean isGmail;
//
//        if(!authRepo.getToken().isEmpty()){
//            token += authRepo.getToken();
//            isGmail = false;
//        }
//        else{
//            token += account.getIdToken();
//            isGmail = true;
//        }
//
//        service.getDetails(token, new RequestModel(isGmail)).enqueue(new Callback<ArrayList<SupplyModel>>() {
//            @Override
//            public void onResponse(Call<ArrayList<SupplyModel>> call, Response<ArrayList<SupplyModel>> response) {
//                if (response.isSuccessful()){
//                    mSupplies.postValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<SupplyModel>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }
}
