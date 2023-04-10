package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.RequestModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SupplyService {

    @POST("/supply/details")
    Call<ArrayList<SupplyModel>> getDetails(@Header("Authorization")String authHeader);

    @POST("/requisition/request-item")
    Call<ResponseModel> pushRequest(@Header("Authorization")String authHeader, @Body RequisitionModel requisition);
}
