package com.dreambig.supplymanagementapp.Views.StockFragments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StockViewModel extends ViewModel {
    private LiveData<ArrayList<SupplyModel>> mSupplies;
    private SupplyRepo repo;

    @Inject
    public StockViewModel(SupplyRepo repo){
        this.repo = repo;
    }

    public void loadmSupplies(){
        if(mSupplies == null){
            repo.loadmSupplies();
            mSupplies = repo.getmSupplies();
            return;
        }
    }

    public void refreshSuppliesData(){
        repo.loadmSupplies();
    }

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }
}