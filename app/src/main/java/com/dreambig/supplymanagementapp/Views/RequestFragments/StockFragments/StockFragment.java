package com.dreambig.supplymanagementapp.Views.RequestFragments.StockFragments;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dreambig.supplymanagementapp.Adapters.SupplyCardAdapter;
import com.dreambig.supplymanagementapp.Adapters.SupplyListAdapter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Utils.ItemTypeStatics;
import com.dreambig.supplymanagementapp.Views.BottomStockDetailsViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentStockBinding;
import com.dreambig.supplymanagementapp.databinding.StockDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StockFragment extends Fragment {

    private StockViewModel mViewModel;
    private BottomStockDetailsViewModel bottomStockDetailsViewModel;
    private FragmentStockBinding binding;
    private SupplyListAdapter supplyListAdapter;
    private SupplyCardAdapter supplyCardAdapter;
    private StockDetailsBinding detailsBinding;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private Integer activeItemType;
    private Integer id_no;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StockViewModel.class);
        bottomStockDetailsViewModel = new ViewModelProvider(this).get(BottomStockDetailsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStockBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //view binding for bottom sheet
        detailsBinding = StockDetailsBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog_custom);
        bottomSheetDialog.setContentView(detailsBinding.getRoot());
        bottomSheetBehavior = bottomSheetDialog.getBehavior();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //Init details
        init();
        mViewModel.init();

        //set status bar
        setStatusBar();

        //Hide keyboard after search
        setOnEditorActionListener();

        //Live data observers
        suppliesObserver();
        sortObserver();

        //Change View
        stockView();

        //check change listener
        checkChangedListener();

        //search
        searchListener();


        supplyCardAdapter.setItemListener(new SupplyCardAdapter.ItemListener() {
            @Override
            public void itemOnClick(SupplyModel supplyItem) {
                setBottomSheetDetails(supplyItem);
            }
        });

        supplyListAdapter.setItemListener(new SupplyListAdapter.ItemListener() {
            @Override
            public void itemOnClick(SupplyModel supplyItem) {
                setBottomSheetDetails(supplyItem);
            }
        });

        mViewModel.loadmAddedItems();
        mViewModel.getmAddedItems().observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                if(itemModels == null || itemModels.size() == 0)
                    return;

                binding.tvViewAddedItems.setText("View Added Items["+ itemModels.size() +"]");
                binding.btnViewAddedItem.setVisibility(View.VISIBLE);
            }
        });

        binding.btnViewAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_stockFragment_to_addedItemsFragment);
            }
        });
    }


    private void checkChangedListener() {
        binding.rgItemType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String item_type;
                if(i == binding.rbRisType.getId()) {
                    item_type = "RIS";
                    activeItemType = ItemTypeStatics.RIS;
                }
                else if(i == binding.rbIcsType.getId()) {
                    item_type = "ICS";
                    activeItemType = ItemTypeStatics.ICS;
                }
                else if(i == binding.rbParType.getId()) {
                    item_type = "PAR";
                    activeItemType = ItemTypeStatics.PAR;
                }
                else {
                    item_type = "ALL";
                    activeItemType = 0;
                }

                mViewModel.sortItemType(binding.etSearch.getText().toString(),activeItemType);
                binding.tvItemType.setText(item_type);
            }
        });
    }

    private void setBottomSheetDetails(SupplyModel supplyItem){
        id_no = null;
        Glide.with(detailsBinding.getRoot())
                .load(supplyItem.getPhoto_url())
                .transform(new FitCenter(), new RoundedCorners(15))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(detailsBinding.ivStockImage);

        detailsBinding.tvProductName.setText(supplyItem.getItem_name());
        detailsBinding.tvItemType.setText("Type: " + supplyItem.getItem_type());
        detailsBinding.tvMeasurement.setText("Unit of Measure: " + supplyItem.getUnit_measurement());
        detailsBinding.tvUnitCost.setText("Unit Cost: ₱ " + String.format("%.2f", supplyItem.getUnit_cost()));
        detailsBinding.tvSubTotal.setText("Sub-total: ₱ 0.00");
        detailsBinding.tvAvailability.setText("Availability: " + supplyItem.getCurrent_supply());
        detailsBinding.tvDescription.setText(supplyItem.getDesc());
        detailsBinding.etNote.setText(null);
        detailsBinding.etNote.clearFocus();
        detailsBinding.etQuantity.setText(null);
        detailsBinding.etQuantity.clearFocus();
        detailsBinding.btnAddItem.setEnabled(false);


        detailsBinding.btnQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = 0;
                try{
                    currentQuantity = Integer.parseInt(detailsBinding.etQuantity.getText().toString());
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

                if(supplyItem.getCurrent_supply() <= currentQuantity)
                    return;

                currentQuantity++;
                detailsBinding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(String.valueOf(currentQuantity)))));
                detailsBinding.etQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        detailsBinding.btnQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = 0;
                try{
                    currentQuantity = Integer.parseInt(detailsBinding.etQuantity.getText().toString());
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

                if(currentQuantity <= 0)
                    return;

                currentQuantity--;
                detailsBinding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(String.valueOf(currentQuantity)))));
                detailsBinding.etQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        detailsBinding.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(detailsBinding.etQuantity.getText().toString().isEmpty()){
                    detailsBinding.tvSubTotal.setText("Sub-total: ₱ 0.00");
                    return;
                }



                detailsBinding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(detailsBinding.etQuantity.getText().toString()))));
            }
        });

        mViewModel.getmAddedItems().observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                detailsBinding.btnAddItem.setEnabled(true);
                if(itemModels == null || itemModels.size() == 0)
                    return;

                for(ItemModel item : itemModels){
                    if(item.getProduct_code().equals(supplyItem.getProduct_code())){
                        id_no = item.getId_no();
                        detailsBinding.etQuantity.setText(String.valueOf(item.getQuantity()));
                        break;
                    }
                }

            }
        });


        detailsBinding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput(detailsBinding.etQuantity.getText().toString(), supplyItem.getCurrent_supply())){

                    mViewModel.insertItems(
                            id_no,
                            supplyItem.getProduct_code(),
                            supplyItem.getItem_type(),
                            supplyItem.getItem_name(),
                            supplyItem.getPhoto_url(),
                            Integer.parseInt(detailsBinding.etQuantity.getText().toString()),
                            supplyItem.getUnit_measurement(),
                            computeTotalCost(supplyItem.getUnit_cost(),Double.parseDouble(detailsBinding.etQuantity.getText().toString())),
                            detailsBinding.etNote.getText().toString()
                    );

                    bottomSheetDialog.dismiss();
                }

            }
        });

        hideKeyboard();
        bottomSheetDialog.show();
    }

    private boolean validateInput(String input_text, int max_supply) {
        if(input_text.isEmpty()){
            return false;
        }

        if(Integer.parseInt(input_text) <= 0){

            return false;
        }

        if(Integer.parseInt(input_text) > max_supply){
            return false;
        }
        return true;
    }

    public Double computeTotalCost(Double unit_cost, Double quantity){
        return unit_cost * quantity;
    }


    private void searchListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.sortItemType(binding.etSearch.getText().toString(), activeItemType);
            }
        });
    }

    private void sortObserver() {
        mViewModel.getmSort().observe(getViewLifecycleOwner(), new Observer<ArrayList<SupplyModel>>() {
            @Override
            public void onChanged(ArrayList<SupplyModel> sortedItems) {
                if(sortedItems == null)
                    return;

                supplyListAdapter.setItems(sortedItems);
                supplyCardAdapter.setItems(sortedItems);
            }
        });
    }

    private void suppliesObserver() {
        mViewModel.loadmSupplies();
        binding.progressBar.setVisibility(View.VISIBLE);
        mViewModel.getmSupplies().observe(getViewLifecycleOwner(), new Observer<ArrayList<SupplyModel>>() {
            @Override
            public void onChanged(ArrayList<SupplyModel> supplyModels) {
                if(supplyModels != null){
                    binding.refreshLayout.setRefreshing(false);
                    mViewModel.sortItemType(binding.etSearch.getText().toString(),activeItemType);
                }
                binding.progressBar.setVisibility(View.INVISIBLE);
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

        Bundle bundle = getArguments();
        int item_type = bundle.getInt("item_type");
        activeItemType = item_type;
        if(item_type == ItemTypeStatics.RIS) binding.rbRisType.setChecked(true);
        else if(item_type == ItemTypeStatics.ICS) binding.rbIcsType.setChecked(true);
        else if(item_type == ItemTypeStatics.PAR) binding.rbParType.setChecked(true);
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
        binding.etSearch.clearFocus();
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
    }

    private void setOnEditorActionListener(){
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                hideKeyboard();
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