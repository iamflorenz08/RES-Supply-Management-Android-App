package com.dreambig.supplymanagementapp.Views.RequestFragments.StockFragments;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dreambig.supplymanagementapp.Adapters.RecommendAdapter;
import com.dreambig.supplymanagementapp.Adapters.SupplyCardAdapter;
import com.dreambig.supplymanagementapp.Adapters.SupplyListAdapter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Utils.ItemTypeStatics;
import com.dreambig.supplymanagementapp.Views.BottomStockDetailsFragment;
import com.dreambig.supplymanagementapp.Views.BottomStockDetailsViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentStockBinding;
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
    private RecommendAdapter recommendAdapter;
    private BottomStockDetailsFragment bottomStockDetailsFragment;
    private Integer activeItemType;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
        bottomStockDetailsViewModel = new ViewModelProvider(requireActivity()).get(BottomStockDetailsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStockBinding.inflate(inflater, container, false);
        bottomStockDetailsFragment = new BottomStockDetailsFragment();
        supplyListAdapter = new SupplyListAdapter();
        supplyCardAdapter = new SupplyCardAdapter();
        recommendAdapter = new RecommendAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init details
        init();
        mViewModel.init();
        bottomStockDetailsViewModel.init();
        mViewModel.loadmAddedItems();
        mViewModel.loadRecommendedItems();
        binding.rvRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvRecommend.setAdapter(recommendAdapter);

        //set status bar
        setStatusBar();

        //Hide keyboard after search
        setOnEditorActionListener();

        //Live data observers
        suppliesObserver();
        sortObserver();

        mViewModel.getRecommendedItemsLivedata().observe(getViewLifecycleOwner(), new Observer<ArrayList<SupplyModel>>() {
            @Override
            public void onChanged(ArrayList<SupplyModel> supplyModels) {
                if(supplyModels.size() <= 0 ||  supplyModels == null){
                    binding.tvRecommend.setVisibility(View.GONE);
                    binding.rvRecommend.setVisibility(View.GONE);
                    return;
                }
                binding.tvRecommend.setVisibility(View.VISIBLE);
                binding.rvRecommend.setVisibility(View.VISIBLE);
                recommendAdapter.setRecommend_items(supplyModels);
            }
        });

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

        recommendAdapter.setRecommendAdapterListener(new RecommendAdapter.RecommendAdapterListener() {
            @Override
            public void OnRecommendItemClick(SupplyModel recommend_item) {
                setBottomSheetDetails(recommend_item);
            }
        });


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
       bottomStockDetailsViewModel.setSupplyModel(supplyItem);
       bottomStockDetailsFragment.show(getActivity().getSupportFragmentManager(), bottomStockDetailsFragment.getTag());
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