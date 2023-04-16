package com.dreambig.supplymanagementapp.Networks;

import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SavedItemService {
    @GET("/api/v2/supply/saved-items/{user_id}")
    Call<ArrayList<SavedItemModel>> getSavedItems(@Path("user_id") String user_id);

    @POST("/api/v2/supply/saved-items")
    Call<ResponseModel> postSavedItems(@Body SavedItemModel savedItemModel);

    @DELETE("/api/v2/supply/saved-items")
    Call<ResponseModel> deleteSavedItems(@Query("id") ArrayList<String> items);
}
