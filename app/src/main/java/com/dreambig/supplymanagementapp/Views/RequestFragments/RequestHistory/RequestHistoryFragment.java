package com.dreambig.supplymanagementapp.Views.RequestFragments.RequestHistory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dreambig.supplymanagementapp.Adapters.RequisitionAdapter;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.MainViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentRequestHistoryBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RequestHistoryFragment extends Fragment {

    private RequestHistoryViewModel mViewModel;
    private MainViewModel mainSharedViewModel;
    private FragmentRequestHistoryBinding binding;
    private RequisitionAdapter requisitionAdapter;
    private String filter = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(RequestHistoryFragment.this).get(RequestHistoryViewModel.class);
        mainSharedViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        requisitionAdapter = new RequisitionAdapter();
        binding.rvRequestContainer.setAdapter(requisitionAdapter);
        binding.rvRequestContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvRequestContainer.setNestedScrollingEnabled(false);

        binding.rgHeader.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbTBA:
                        filter = "To be approved";
                        break;
                    case R.id.rbOnGoing:
                        filter = "On going";
                        break;
                    case R.id.rbRejected:
                        filter = "Rejected";
                        break;
                    case R.id.rbCompleted:
                        filter = "Completed";
                        break;
                }
                mViewModel.refresh();
            }
        });
        binding.rbTBA.setChecked(true);


        mViewModel.init();
        mViewModel.loadRequisitions();
        mViewModel.getRequisitions().observe(getViewLifecycleOwner(), new Observer<List<RequisitionModel>>() {
            @Override
            public void onChanged(List<RequisitionModel> requisitionModels) {
                ArrayList<RequisitionModel> filtered = new ArrayList<>();
                for(RequisitionModel requisition : requisitionModels){
                    if(requisition.getStatus().equals(filter)) filtered.add(requisition);
                }
                requisitionAdapter.setRequisitions(filtered);
            }
        });


        requisitionAdapter.setItemListener(new RequisitionAdapter.ItemListener() {
            @Override
            public void requestDetails(RequisitionModel requisitionModel) {
                mainSharedViewModel.setRequest(requisitionModel);
                Navigation.findNavController(getView()).navigate(R.id.action_requestHistoryFragment_to_requisitionDetailsFragment);
            }
        });

    }
}