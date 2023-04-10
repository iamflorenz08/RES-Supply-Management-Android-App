package com.dreambig.supplymanagementapp.Views.RequestFragments.RequestHistory;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.Networks.RequisitionService;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.RequisitionRepo;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RequestHistoryViewModel extends ViewModel {
    private RequisitionRepo requisitionRepo;
    private AuthRepo authRepo;
    private SupplyRepo supplyRepo;
    private MutableLiveData<List<RequisitionModel>> requisitions;

    @Inject
    RequestHistoryViewModel(RequisitionRepo requisitionRepo, AuthRepo authRepo, SupplyRepo supplyRepo){
        this.requisitionRepo = requisitionRepo;
        this.authRepo = authRepo;
        this.supplyRepo = supplyRepo;
    }

    public void init(){
        requisitions = requisitionRepo.getRequisitions();
    }

    public void loadRequisitions(){
        requisitionRepo.loadRequisitions(authRepo.getAuthenticatedUser().getValue().get_id());
    }

    public void refresh(){
        if(requisitions == null) return;
        requisitions.setValue(requisitions.getValue());
    }

    public LiveData<List<RequisitionModel>> getRequisitions() {
        return requisitions;
    }
}