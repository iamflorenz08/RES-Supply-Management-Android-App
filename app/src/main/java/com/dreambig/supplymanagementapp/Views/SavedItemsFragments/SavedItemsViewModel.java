package com.dreambig.supplymanagementapp.Views.SavedItemsFragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.SavedItemRepo;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SavedItemsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private SavedItemRepo savedItemRepo;
    private AuthRepo authRepo;
    private LiveData<ArrayList<SavedItemModel>> savedItemsLiveData;
    private ArrayList<SavedItemModel> deleteItems;

    @Inject
    public SavedItemsViewModel(SavedItemRepo savedItemRepo, AuthRepo authRepo){
        this.savedItemRepo = savedItemRepo;
        this.authRepo = authRepo;
    }

    public void init(){
        deleteItems = new ArrayList<>();
        if(savedItemsLiveData == null){
            savedItemsLiveData = savedItemRepo.getSavedItemsLiveData();
        }
    }

    public Boolean deleteSavedItems(){
        if (deleteItems.size() != 0){
            ArrayList<String> items = new ArrayList<>();
            String user_id = authRepo.getAuthenticatedUser().getValue().get_id();
            for(SavedItemModel item : deleteItems){
                items.add(item.get_id());
            }
            savedItemRepo.deleteSavedItems(user_id,items);
            deleteItems.clear();
            return true;
        }

        return false;
    }

    public void getSavedItems(){
        String user_id = authRepo.getAuthenticatedUser().getValue().get_id();
        savedItemRepo.getSavedItems(user_id);
    }

    public void setDeleteItems(ArrayList<SavedItemModel> deleteItems) {
        this.deleteItems = deleteItems;
    }

    public LiveData<ArrayList<SavedItemModel>> getSavedItemsLiveData() {
        return savedItemsLiveData;
    }

}