package com.dreambig.supplymanagementapp.Views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.SavedItemRepo;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BottomStockDetailsViewModel extends ViewModel {
    private SavedItemRepo savedItemRepo;
    private AuthRepo authRepo;
    private MutableLiveData<SupplyModel> supplyModel;

    @Inject
    public BottomStockDetailsViewModel(SavedItemRepo savedItemRepo, AuthRepo authRepo){
        this.savedItemRepo = savedItemRepo;
        this.authRepo = authRepo;
    }

    public void init(){
        if(supplyModel == null){
            supplyModel = new MutableLiveData<>();
        }
    }


    public void saveItem(SupplyModel supplyModel){
        String user = authRepo.getAuthenticatedUser().getValue().get_id();
        savedItemRepo.saveItem(new SavedItemModel(user, supplyModel));
    }

    public void unsaveItem(String supply_id) {
        String user_id = authRepo.getAuthenticatedUser().getValue().get_id();
        ArrayList<String> items = new ArrayList<>();
        for(SavedItemModel savedItem : savedItemRepo.getSavedItemsLiveData().getValue()){
            if(savedItem.getItem().get_id().equals(supply_id)){
                items.add(savedItem.get_id());
                savedItemRepo.deleteSavedItems(user_id,items);
                return;
            }
        }
    }

    public Boolean isItemSaved(String supply_id){
        String user_id = authRepo.getAuthenticatedUser().getValue().get_id();
        if(savedItemRepo.getSavedItemsLiveData().getValue() == null) {
            savedItemRepo.getSavedItems(user_id);
            return false;
        }

        for(SavedItemModel savedItem : savedItemRepo.getSavedItemsLiveData().getValue()){
            if(savedItem.getItem().get_id().equals(supply_id)){
                return true;
            }
        }

        return false;
    }

    public void setSupplyModel(SupplyModel supplyModel) {
        this.supplyModel.setValue(supplyModel);
    }

    public LiveData<SupplyModel> getSupplyModel() {
        return supplyModel;
    }


}
