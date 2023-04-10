package com.dreambig.supplymanagementapp.Views.RequestFragments.AddedItemsFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;
import com.dreambig.supplymanagementapp.Views.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddedItemsViewModel extends ViewModel {
    private SupplyRepo supplyRepo;
    private AuthRepo authRepo;
    private LiveData<List<ItemModel>> mAddedItems;
    private LiveData<ResponseModel> pushRequestResponse;

    @Inject
    public AddedItemsViewModel(SupplyRepo supplyRepo, AuthRepo authRepo){
        this.supplyRepo = supplyRepo;
        this.authRepo = authRepo;
    }

    public void init(){
        if(pushRequestResponse == null)
            pushRequestResponse = supplyRepo.getPushRequestResponse();
    }

    public void loadmAddedItems(){
        supplyRepo.loadmAddedItems(authRepo.getAuthenticatedUser().getValue().get_id());
        if(mAddedItems == null)
            mAddedItems = supplyRepo.getmAddedItems();
    }

    public void insertItem(ItemModel itemModel){
        Log.d("my_dev", String.valueOf(itemModel.getTotal_cost()));
        supplyRepo.insertItems(itemModel);
    }

    public LiveData<List<ItemModel>> getmAddedItems() {
        return mAddedItems;
    }

    public void deleteItem(ItemModel item) {
        supplyRepo.deleteItem(item);
    }

    public void pushRequest() {
        if(mAddedItems == null || mAddedItems.getValue().size() == 0)
            return;

        supplyRepo.pushRequest(new RequisitionModel(
                authRepo.getAuthenticatedUser().getValue().get_id(),
                "To be approved",
                mAddedItems.getValue()
        ));
    }

    public LiveData<ResponseModel> getPushRequestResponse() {
        return pushRequestResponse;
    }
}