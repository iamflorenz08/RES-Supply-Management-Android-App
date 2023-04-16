package com.dreambig.supplymanagementapp.Views.SavedItemsFragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.Adapters.SavedItemsAdapter;
import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.BottomStockDetailsFragment;
import com.dreambig.supplymanagementapp.Views.BottomStockDetailsViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentSavedItemsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedItemsFragment extends Fragment implements SavedItemsAdapter.SavedItemsListener{

    private FragmentSavedItemsBinding binding;
    private SavedItemsViewModel savedItemsViewModel;
    private BottomStockDetailsFragment bottomStockDetailsFragment;
    private BottomStockDetailsViewModel bottomStockDetailsViewModel;
    private SavedItemsAdapter savedItemsAdapter;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedItemsViewModel = new ViewModelProvider(this).get(SavedItemsViewModel.class);
        bottomStockDetailsViewModel = new ViewModelProvider(requireActivity()).get(BottomStockDetailsViewModel.class);
        bottomStockDetailsViewModel.init();
        savedItemsViewModel.init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedItemsBinding.inflate(inflater, container, false);

        bottomStockDetailsFragment = new BottomStockDetailsFragment();
        savedItemsAdapter = new SavedItemsAdapter(this);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mvDeleteDialog);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvSavedItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvSavedItems.setAdapter(savedItemsAdapter);
        savedItemsViewModel.getSavedItems();
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setDraggable(false);


        savedItemsViewModel.getSavedItemsLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<SavedItemModel>>() {
            @Override
            public void onChanged(ArrayList<SavedItemModel> savedItemModels) {
                savedItemsAdapter.setSavedItems(savedItemModels);
            }
        });


        binding.cbEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean editMode) {
                if(editMode){
                    savedItemsAdapter.setEditMode(true);
                    binding.cbEdit.setText("Done");
                    binding.cbEdit.setTextColor(getResources().getColor(R.color.primaryVariantLight));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    binding.rvSavedItems.setPadding(0,0,0, binding.mvDeleteDialog.getHeight() + 5);
                }
                else{
                    savedItemsAdapter.setEditMode(false);
                    binding.cbEdit.setText("Edit");
                    binding.cbEdit.setTextColor(Color.BLACK);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    binding.rvSavedItems.setPadding(0,0,0, 5);
                }
            }
        });

        binding.mvDeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!savedItemsViewModel.deleteSavedItems()){
                    Toast.makeText(getContext(), "Select an item to remove", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.mvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cbEdit.setChecked(false);
            }
        });



    }



    @Override
    public void OnEditItemClick(ArrayList<SavedItemModel> deleteItems) {
        savedItemsViewModel.setDeleteItems(deleteItems);
    }

    @Override
    public void OnItemLongClick() {
        binding.cbEdit.setChecked(true);
    }

    @Override
    public void OnItemClick(SavedItemModel savedItemModel) {
        bottomStockDetailsViewModel.setSupplyModel(savedItemModel.getItem());
        bottomStockDetailsFragment.show(getActivity().getSupportFragmentManager(), bottomStockDetailsFragment.getTag());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}