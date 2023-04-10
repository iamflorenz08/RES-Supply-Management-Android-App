package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.RequisitionModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequisitionService {

    @GET("/requisition")
    Call<ArrayList<RequisitionModel>> loadRequisitions(@Query("user_id") String user_id);
}
