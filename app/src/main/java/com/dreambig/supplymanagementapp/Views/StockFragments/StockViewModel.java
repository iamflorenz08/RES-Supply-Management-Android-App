package com.dreambig.supplymanagementapp.Views.StockFragments;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StockViewModel extends ViewModel {
    private MutableLiveData<ArrayList<SupplyModel>> mSearch;
    private LiveData<ArrayList<SupplyModel>> mSupplies;
    private ArrayList<SupplyModel> supplies;
    private SupplyRepo repo;

    @Inject
    public StockViewModel(SupplyRepo repo){
        this.repo = repo;
    }

    public void init(){
        if (mSearch == null)
            mSearch = new MutableLiveData<>();

        if(mSupplies == null)
            mSupplies = repo.getmSupplies();

        supplies = new ArrayList<>();
    }

    public void loadmSupplies(){
        repo.loadmSupplies();
    }

    public void refreshSuppliesData(){
        repo.loadmSupplies();
    }

    public void search(String text){
        supplies.clear();
        for(SupplyModel item : mSupplies.getValue()){
            if(item.getItem_name().toLowerCase().contains(text.toLowerCase()))
                supplies.add(item);
        }
        mSearch.setValue(supplies);
    }

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }

    public LiveData<ArrayList<SupplyModel>> getmSearch() {
        return mSearch;
    }
}