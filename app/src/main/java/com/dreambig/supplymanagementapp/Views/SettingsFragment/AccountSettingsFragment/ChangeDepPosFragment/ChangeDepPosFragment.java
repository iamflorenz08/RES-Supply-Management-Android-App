package com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment.ChangeDepPosFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment.AccountSettingsViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentChangeDepPosBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChangeDepPosFragment extends Fragment {

    private FragmentChangeDepPosBinding binding;
    private AccountSettingsViewModel mViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeDepPosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);
        mViewModel.init();

        //observes user live data
        userLiveData();

        //back click listener
        backListener();

        //populate department
        populateDepartment();

        submitListener();
    }

    private void userLiveData() {
        mViewModel.getmUserInfo().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                binding.atvDepartment.setText(userModel.getDepartment(), false);
                binding.etPosition.setText(userModel.getPosition());
            }
        });
    }

    private void populateDepartment() {
        String[] departmentLists = {"Select", "Kindergarten", "Grade 1", "Grade 2",
                "Grade3", "Grade 4", "Grade 5", "Grade 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, departmentLists);
        binding.atvDepartment.setAdapter(adapter);
    }

    private void backListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void submitListener(){
        binding.btnChangeDepPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){

                }
            }
        });
    }

    private boolean isValidated(){
        boolean isValid = true;
        binding.atvDepartment.setError(null);
        binding.etPosition.setError(null);

        if (binding.atvDepartment.getText().toString().equals("Select")){
            binding.atvDepartment.setError("Please choose your department");
            isValid = false;
        }
        if (binding.etPosition.getText().toString().trim().isEmpty()){
            binding.etPosition.setError("Please enter your position");
            isValid = false;
        }
        return isValid;
    }
}