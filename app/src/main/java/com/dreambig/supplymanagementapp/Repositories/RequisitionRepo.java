package com.dreambig.supplymanagementapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Networks.RequisitionService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionRepo {
    RequisitionService requisitionService;
    private MutableLiveData<List<RequisitionModel>> requisitions = new MutableLiveData<>();

    public RequisitionRepo(RequisitionService requisitionService){
        this.requisitionService = requisitionService;
    }

    public void loadRequisitions(String user_id){
        requisitionService.loadRequisitions(user_id).enqueue(new Callback<ArrayList<RequisitionModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RequisitionModel>> call, Response<ArrayList<RequisitionModel>> response) {
                if(response.isSuccessful()){
                    requisitions.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequisitionModel>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<RequisitionModel>> getRequisitions() {
        return requisitions;
    }
}
