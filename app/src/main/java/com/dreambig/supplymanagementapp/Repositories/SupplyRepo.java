package com.dreambig.supplymanagementapp.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Networks.SupplyService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyRepo {
    private static SupplyRepo instance;
    private SupplyService service;
    private MutableLiveData<ArrayList<SupplyModel>> mSupplies;

    public static SupplyRepo getInstance(SupplyService service){
        if(instance==null){
            instance = new SupplyRepo(service);
        }
        return instance;
    }

    private SupplyRepo(SupplyService service){
        this.service = service;
        mSupplies = new MutableLiveData<>();
    }

    public void loadmSupplies(){
        service.getDetails().enqueue(new Callback<ArrayList<SupplyModel>>() {
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

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }
}
