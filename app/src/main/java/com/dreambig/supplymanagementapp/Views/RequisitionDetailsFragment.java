package com.dreambig.supplymanagementapp.Views;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Adapters.RequisitionAdapter;
import com.dreambig.supplymanagementapp.Adapters.RequisitionDetailAdapter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentRequisitionDetailsBinding;

import java.time.OffsetDateTime;


public class RequisitionDetailsFragment extends Fragment {

    private FragmentRequisitionDetailsBinding binding;
    private MainViewModel mainSharedViewModel;
    private RequisitionDetailAdapter requisitionDetailAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequisitionDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainSharedViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        requisitionDetailAdapter = new RequisitionDetailAdapter();
        binding.rvRequisitionItems.setAdapter(requisitionDetailAdapter);
        binding.rvRequisitionItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvRequisitionItems.setNestedScrollingEnabled(false);

        mainSharedViewModel.getRequest().observe(getViewLifecycleOwner(), new Observer<RequisitionModel>() {
            @Override
            public void onChanged(RequisitionModel requisitionModel) {
                setUpDetails(requisitionModel);
            }
        });
    }

    private void setUpDetails(RequisitionModel requisition) {
        Drawable drawable = null;
        Double total_cost = 0.0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            OffsetDateTime odt = OffsetDateTime.parse(requisition.getCreatedAt());
            binding.tvDate.setText(odt.getMonth() + " " + odt.getDayOfMonth() + ", " + odt.getYear());
        }

        for(ItemModel item : requisition.getItems()){
            total_cost += item.getTotal_cost();
        }

        binding.tvTotalCost.setText("₱ " + String.format("%.2f", total_cost));
        binding.tvStatus.setText(requisition.getStatus());
        binding.llNote.setVisibility(View.VISIBLE);
        if(requisition.getStatus().equals("To be approved")){
            drawable = getResources().getDrawable(R.drawable.tba_icon_inactive);
            binding.clBgStatus.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.llNote.setVisibility(View.GONE);
        }
        else if(requisition.getStatus().equals("On going")){
            drawable = getResources().getDrawable(R.drawable.check_icon_inactive);
            binding.clBgStatus.setBackgroundColor(Color.parseColor("#32A05F"));
        }
        else if(requisition.getStatus().equals("Rejected")){
            drawable = getResources().getDrawable(R.drawable.x_icon_inactive);
            binding.clBgStatus.setBackgroundColor(Color.parseColor("#FF1A03"));
        }
        else if(requisition.getStatus().equals("Completed")){
            drawable = getResources().getDrawable(R.drawable.completed_icon_inactive);
            binding.clBgStatus.setBackgroundColor(Color.parseColor("#32A05F"));
        }
        binding.tvStatus.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);

        requisitionDetailAdapter.setItems(requisition.getItems());





    }
}