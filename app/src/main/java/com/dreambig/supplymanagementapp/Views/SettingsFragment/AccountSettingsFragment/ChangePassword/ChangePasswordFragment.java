package com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment.ChangePassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentChangePasswordBinding;


public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backListener();
        submitListener();
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
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){

                }
            }
        });
    }

    private boolean isValidated(){
        boolean isValid = true;
        binding.etCurrentPassword.setError(null);
        binding.etNewPassword.setError(null);
        binding.etRetypePassword.setError(null);

        if (binding.etCurrentPassword.getText().toString().isEmpty())
        {
            binding.etCurrentPassword.setError("Please enter current password");
            isValid = false;
        }
        if (binding.etNewPassword.getText().toString().isEmpty())
        {
            binding.etNewPassword.setError("Please enter new password");
            isValid = false;
        }
        if (binding.etRetypePassword.getText().toString().isEmpty())
        {
            binding.etRetypePassword.setError("Please retype new password");
            isValid = false;
        }

        return isValid;
    }

}