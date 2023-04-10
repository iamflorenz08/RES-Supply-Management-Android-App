package com.dreambig.supplymanagementapp.Repositories;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreambig.supplymanagementapp.Locals.RequisitionRoom.ItemDatabase;
import com.dreambig.supplymanagementapp.Locals.Token;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Networks.SupplyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyRepo {
    private SupplyService service;
    private AuthRepo authRepo;
    private Token token;
    private ItemDatabase itemDatabase;
    private MutableLiveData<ArrayList<SupplyModel>> mSupplies;
    private LiveData<List<ItemModel>> mAddedItems;
    private MutableLiveData<ResponseModel> pushRequestResponse;

    public SupplyRepo(SupplyService service, AuthRepo authRepo, ItemDatabase itemDatabase, Token token){
        this.service = service;
        this.authRepo = authRepo;
        this.itemDatabase = itemDatabase;
        this.token = token;
        mSupplies = new MutableLiveData<>();
        pushRequestResponse = new MutableLiveData<>();
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

    public void insertItems(ItemModel itemModel){
        new insertItems().execute(itemModel);
    }

    public void deleteItem(ItemModel item) {
        new deleteItems().execute(item);
    }

    public void loadmAddedItems(String user_id){
        mAddedItems = itemDatabase.requistionDAO().loadAddedItems(user_id);
    }

    public void deleteAll(){
        new deleteAll().execute(authRepo.getAuthenticatedUser().getValue().get_id());
    }


    public LiveData<List<ItemModel>> getmAddedItems() {

        return mAddedItems;
    }

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }

    public void pushRequest(RequisitionModel request) {
        service.pushRequest("Bearer " + token.readToken(), request).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    pushRequestResponse.postValue(response.body());
                    if(!response.body().getError()){
                        deleteAll();
                    }
                    return;
                }
                pushRequestResponse.postValue(null);
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                pushRequestResponse.postValue(null);
            }
        });
    }

    public MutableLiveData<ResponseModel> getPushRequestResponse() {
        pushRequestResponse.setValue(null);
        return pushRequestResponse;
    }

    private class insertItems extends AsyncTask<ItemModel,Void,Void>{
        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemDatabase.requistionDAO().insertItems(itemModels[0]);
            return null;
        }
    }

    private class deleteItems extends AsyncTask<ItemModel,Void,Void>{
        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemDatabase.requistionDAO().deleteItems(itemModels[0]);
            return null;
        }
    }

    private class deleteAll extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... user_id) {
            itemDatabase.requistionDAO().deleteAll(user_id[0]);
            return null;
        }
    }
}
