package com.dreambig.supplymanagementapp.Views.RequestFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Utils.ItemTypeStatics;
import com.dreambig.supplymanagementapp.databinding.FragmentChooseBinding;


public class ChooseFragment extends Fragment implements View.OnClickListener {

    private FragmentChooseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChooseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRIS.setOnClickListener(this);
        binding.btnICS.setOnClickListener(this);
        binding.btnPAR.setOnClickListener(this);

        binding.btnRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chooseFragment_to_requestHistoryFragment);
            }
        });

        binding.btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chooseFragment_to_savedItemsFragment);
            }
        });

    }

    @Override
    public void onClick(View view) {
        Log.d("MY_DEV", String.valueOf(view.getId()));
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.btnRIS:
                bundle.putInt("item_type", ItemTypeStatics.RIS);
                break;
            case R.id.btnICS:
                bundle.putInt("item_type", ItemTypeStatics.ICS);
                break;
            case R.id.btnPAR:
                bundle.putInt("item_type", ItemTypeStatics.PAR);
                break;
        }

        Navigation.findNavController(view).navigate(R.id.action_chooseFragment_to_stockFragment, bundle);

    }
}