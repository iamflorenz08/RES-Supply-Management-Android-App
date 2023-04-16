package com.dreambig.supplymanagementapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.Networks.SavedItemService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedItemRepo {
    private SavedItemService savedItemService;
    private MutableLiveData<ResponseModel> deleteResponse;
    private MutableLiveData<ArrayList<SavedItemModel>> savedItemsLiveData;

    public SavedItemRepo(SavedItemService savedItemService){
        this.savedItemService = savedItemService;
        this.deleteResponse = new MutableLiveData<>();
        this.savedItemsLiveData = new MutableLiveData<>();
    }

    public void deleteSavedItems(String user_id, ArrayList<String> items){
        savedItemService.deleteSavedItems(items).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    getSavedItems(user_id);
                    return;
                }
                Log.d("my_dev", "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d("my_dev", "onResponse: " + t.getMessage());
            }
        });
    }

    public void getSavedItems(String user_id) {
        savedItemService.getSavedItems(user_id).enqueue(new Callback<ArrayList<SavedItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SavedItemModel>> call, Response<ArrayList<SavedItemModel>> response) {
                if(response.isSuccessful()){
                    savedItemsLiveData.postValue(response.body());
                    return;
                }
                Log.d("my_dev", "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<ArrayList<SavedItemModel>> call, Throwable t) {
                Log.d("my_dev", "onResponse: " + t.getMessage());
            }
        });
    }

    public void saveItem(SavedItemModel savedItemModel) {
        savedItemService.postSavedItems(savedItemModel).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    getSavedItems(savedItemModel.getUser());
                    return;
                }
                Log.d("my_dev", "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d("my_dev", "onResponse: " + t.getMessage());
            }
        });
    }


    public LiveData<ResponseModel> getDeleteResponse() {
        return deleteResponse;
    }

    public LiveData<ArrayList<SavedItemModel>> getSavedItemsLiveData() {
        return savedItemsLiveData;
    }
}
