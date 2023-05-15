package com.dreambig.supplymanagementapp.Views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.RequestFragments.StockFragments.StockViewModel;
import com.dreambig.supplymanagementapp.databinding.StockDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BottomStockDetailsFragment extends BottomSheetDialogFragment {

    private StockDetailsBinding binding;
    private BottomStockDetailsViewModel bottomStockDetailsViewModel;
    private StockViewModel stockViewModel;
    private Integer id_no;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog_custom);
        bottomStockDetailsViewModel = new ViewModelProvider(requireActivity()).get(BottomStockDetailsViewModel.class);
        stockViewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
        stockViewModel.init();
        stockViewModel.loadmAddedItems();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setSkipCollapsed(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StockDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomStockDetailsViewModel.getSupplyModel().observe(getViewLifecycleOwner(), new Observer<SupplyModel>() {
            @Override
            public void onChanged(SupplyModel supplyModel) {
                setDetails(supplyModel);
            }
        });


    }

    private void setDetails(SupplyModel supplyItem) {
        id_no = null;
        Glide.with(binding.getRoot())
                .load(supplyItem.getPhoto_url())
                .transform(new FitCenter(), new RoundedCorners(15))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivStockImage);

        binding.tvProductName.setText(supplyItem.getItem_name());
        binding.tvItemType.setText("Type: " + supplyItem.getItem_type());
        binding.tvMeasurement.setText("Unit of Measure: " + supplyItem.getUnit_measurement());
        binding.tvUnitCost.setText("Unit Cost: ₱ " + String.format("%.2f", supplyItem.getUnit_cost()));
        binding.tvSubTotal.setText("Sub-total: ₱ 0.00");
        binding.tvAvailability.setText("Availability: " + (supplyItem.getCurrent_supply() - supplyItem.getBuffer()));
        binding.tvDescription.setText(supplyItem.getDesc());
        binding.etNote.setText(null);
        binding.etNote.clearFocus();
        binding.etQuantity.setText(null);
        binding.etQuantity.clearFocus();
        binding.btnAddItem.setEnabled(false);
        binding.cbSavedItem.setChecked(false);

        if(bottomStockDetailsViewModel.isItemSaved(supplyItem.get_id())){
            binding.cbSavedItem.setChecked(true);
        }

        binding.cbSavedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSaved) {
                if(isSaved){
                    bottomStockDetailsViewModel.saveItem(supplyItem);
                    return;
                }

                bottomStockDetailsViewModel.unsaveItem(supplyItem.get_id());
                return;
            }
        });

        binding.btnQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = 0;
                try{
                    currentQuantity = Integer.parseInt(binding.etQuantity.getText().toString());
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

                if((supplyItem.getCurrent_supply() - supplyItem.getBuffer()) <= currentQuantity)
                    return;

                currentQuantity++;
                binding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(String.valueOf(currentQuantity)))));
                binding.etQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        binding.btnQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = 0;
                try{
                    currentQuantity = Integer.parseInt(binding.etQuantity.getText().toString());
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

                if(currentQuantity <= 0)
                    return;

                currentQuantity--;
                binding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(String.valueOf(currentQuantity)))));
                binding.etQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        binding.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(binding.etQuantity.getText().toString().isEmpty()){
                    binding.tvSubTotal.setText("Sub-total: ₱ 0.00");
                    return;
                }
                binding.tvSubTotal.setText("Sub-total: ₱ " + String.format("%.2f",computeTotalCost(supplyItem.getUnit_cost(),
                        Double.parseDouble(binding.etQuantity.getText().toString()))));
            }
        });

        stockViewModel.getmAddedItems().observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                binding.btnAddItem.setEnabled(true);
                if(itemModels == null || itemModels.size() == 0)
                    return;

                for(ItemModel item : itemModels){
                    if(item.getProduct_code().equals(supplyItem.getProduct_code())){
                        id_no = item.getId_no();
                        binding.etQuantity.setText(String.valueOf(item.getQuantity()));
                        break;
                    }
                }

            }
        });

        binding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput(binding.etQuantity.getText().toString(), (supplyItem.getCurrent_supply() - supplyItem.getBuffer()))){

                    stockViewModel.insertItems(
                            id_no,
                            supplyItem.getProduct_code(),
                            supplyItem.getItem_type(),
                            supplyItem.getItem_name(),
                            supplyItem.getPhoto_url(),
                            Integer.parseInt(binding.etQuantity.getText().toString()),
                            supplyItem.getUnit_measurement(),
                            computeTotalCost(supplyItem.getUnit_cost(),Double.parseDouble(binding.etQuantity.getText().toString())),
                            binding.etNote.getText().toString()
                    );
                    dismiss();
                }
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding =  null;
    }
}
