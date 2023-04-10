package com.dreambig.supplymanagementapp.Views.RequestFragments.StockFragments;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;
import com.dreambig.supplymanagementapp.Utils.ItemTypeStatics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StockViewModel extends ViewModel {
    private SupplyRepo supplyRepo;
    private AuthRepo authRepo;
    private MutableLiveData<ArrayList<SupplyModel>> mSort;
    private LiveData<ArrayList<SupplyModel>> mSupplies;
    private LiveData<List<ItemModel>> mAddedItems;
    private ArrayList<SupplyModel> supplies;


    @Inject
    public StockViewModel(SupplyRepo supplyRepo, AuthRepo authRepo){
        this.supplyRepo = supplyRepo;
        this.authRepo = authRepo;
    }

    public void init(){
        if (mSort == null)
            mSort = new MutableLiveData<>();

        if(mSupplies == null)
            mSupplies = supplyRepo.getmSupplies();


        supplies = new ArrayList<>();
    }

    public void loadmSupplies(){
        supplyRepo.loadmSupplies();
    }

    public void refreshSuppliesData(){
        supplyRepo.loadmSupplies();
    }

    public void sortItemType(String text, Integer item_type){
        if(mSupplies.getValue() == null)
            return;

        supplies.clear();
        String item_type_string = "";
        if(item_type == ItemTypeStatics.RIS)
            item_type_string = "RIS";
        else if(item_type == ItemTypeStatics.ICS)
            item_type_string = "ICS";
        else if(item_type == ItemTypeStatics.PAR)
            item_type_string = "PAR";

        for(SupplyModel item : mSupplies.getValue()){
            if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) && item.getItem_type().contains(item_type_string))
                supplies.add(item);
        }
        mSort.setValue(supplies);
    }

    public void insertItems(
            @Nullable Integer id_no,
            String product_code,
            String item_type,
            String item_name,
            String item_image,
            Integer quantity,
            String unit_measurement,
            Double total_cost,
            String notes
    ){
        ItemModel itemModel = new ItemModel();
        if(id_no!=null){
            itemModel.setId_no(id_no);
        }
        itemModel.setUser_id(authRepo.getAuthenticatedUser().getValue().get_id());
        itemModel.setProduct_code(product_code);
        itemModel.setItem_type(item_type);
        itemModel.setItem_name(item_name);
        itemModel.setItem_image(item_image);
        itemModel.setQuantity(quantity);
        itemModel.setUnit_measurement(unit_measurement);
        itemModel.setTotal_cost(total_cost);
        itemModel.setNotes(notes);

        supplyRepo.insertItems(itemModel);
    }
    public void loadmAddedItems(){
        supplyRepo.loadmAddedItems(authRepo.getAuthenticatedUser().getValue().get_id());
        if(mAddedItems == null)
            mAddedItems = supplyRepo.getmAddedItems();
    }

    public LiveData<ArrayList<SupplyModel>> getmSupplies() {
        return mSupplies;
    }

    public LiveData<ArrayList<SupplyModel>> getmSort() {
        return mSort;
    }

    public LiveData<List<ItemModel>> getmAddedItems() {
        return mAddedItems;
    }
}