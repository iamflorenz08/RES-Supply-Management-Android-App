package com.dreambig.supplymanagementapp.Views.RequestFragments.AddedItemsFragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.Adapters.AddedItemsAdapter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Utils.LoadingDialog;
import com.dreambig.supplymanagementapp.databinding.FragmentAddedItemsBinding;
import com.dreambig.supplymanagementapp.databinding.ItemTableRowBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddedItemsFragment extends Fragment {

    private FragmentAddedItemsBinding binding;
    private AddedItemsViewModel mViewModel;
    private LoadingDialog dialog;
    private AddedItemsAdapter addedItemsAdapter;
    public static AddedItemsFragment newInstance() {
        return new AddedItemsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddedItemsBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddedItemsViewModel.class);

        init();

        //observe added item changes
        addedItemsLiveData();

        //observe every successful response
        pushRequestResponse();

        addedItemsAdapter.setItemListener(new AddedItemsAdapter.ItemListener() {
            @Override
            public void insertItem(ItemModel itemModel) {
                mViewModel.insertItem(itemModel);
            }

            @Override
            public void deleteItem(ItemModel itemModel) {
                mViewModel.deleteItem(itemModel);
            }
        });

        binding.btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                mViewModel.pushRequest();
            }
        });
    }

    private void pushRequestResponse() {
        mViewModel.getPushRequestResponse().observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                dialog.dismiss();
                if(responseModel == null)
                    return;
                if(responseModel.getError()){
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addedItemsFragment_to_requestSubmittedFragment);
            }
        });
    }

    private void addedItemsLiveData() {
        mViewModel.getmAddedItems().observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                addedItemsAdapter.setItems(itemModels);
                Double total_cost = 0.0;
                for(ItemModel itemModel : itemModels){
                    total_cost += itemModel.getTotal_cost();
                }
                binding.tvTotalCost.setText("₱ " + String.format("%.2f",total_cost));
            }
        });
    }

    public void init(){
        mViewModel.init();
        mViewModel.loadmAddedItems();
        dialog = new LoadingDialog(getContext());

        addedItemsAdapter = new AddedItemsAdapter();
        binding.rvAddedItems.setAdapter(addedItemsAdapter);
        binding.rvAddedItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvAddedItems.setNestedScrollingEnabled(false);
    }


}