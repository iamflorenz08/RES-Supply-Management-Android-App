package com.dreambig.supplymanagementapp.Views.StockFragments;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dreambig.supplymanagementapp.Adapters.SupplyCardAdapter;
import com.dreambig.supplymanagementapp.Adapters.SupplyListAdapter;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentStockBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StockFragment extends Fragment {

    private StockViewModel mViewModel;
    private FragmentStockBinding binding;
    private SupplyListAdapter supplyListAdapter;
    private SupplyCardAdapter supplyCardAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStockBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StockViewModel.class);

        //Init details
        init();

        //set status bar
        setStatusBar();

        //Hide keyboard after search
        hideKeyboard();

        //Live data observers
        suppliesObserver();

        //Change View
        stockView();
    }

    private void suppliesObserver() {
        mViewModel.loadmSupplies();
        mViewModel.getmSupplies().observe(getViewLifecycleOwner(), new Observer<ArrayList<SupplyModel>>() {
            @Override
            public void onChanged(ArrayList<SupplyModel> supplyModels) {
                if(supplyModels != null){
                    supplyListAdapter.setItems(supplyModels);
                    supplyCardAdapter.setItems(supplyModels);
                    binding.refreshLayout.setRefreshing(false);
                }
                Log.d("MY_DEV","working");
            }
        });
    }

    private void init(){
        binding.rvItems.setNestedScrollingEnabled(false);
        supplyListAdapter = new SupplyListAdapter();
        supplyCardAdapter = new SupplyCardAdapter();

        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refreshSuppliesData();
            }
        });

    }

    private void stockView() {
        LinearLayoutManager listLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayout = new GridLayoutManager(getContext(), 2);

        if(binding.switchView.isChecked()){
            binding.rvItems.setLayoutManager(listLinearLayout);
            binding.rvItems.setAdapter(supplyListAdapter);
        }
        else{
            binding.rvItems.setLayoutManager(gridLayout);
            binding.rvItems.setAdapter(supplyCardAdapter);
        }

        binding.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.switchView.isChecked()){
                    binding.rvItems.setLayoutManager(listLinearLayout);
                    binding.rvItems.setAdapter(supplyListAdapter);
                }
                else{
                    binding.rvItems.setLayoutManager(gridLayout);
                    binding.rvItems.setAdapter(supplyCardAdapter);
                }
            }
        });
    }

    private void hideKeyboard(){
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                binding.etSearch.clearFocus();
                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
                return false;
            }
        });
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

}