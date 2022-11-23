package com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment.ChangeEmailFragment;

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
import android.widget.Toast;

import com.dreambig.supplymanagementapp.Models.PasswordModel;
import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment.AccountSettingsViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentChangeEmailBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChangeEmailFragment extends Fragment {

    private FragmentChangeEmailBinding binding;
    private AccountSettingsViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeEmailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);
        mViewModel.init();

        //observes user data
        userLiveData();

        //back click listener
        backListener();

        submitListener();
    }

    private void userLiveData() {
        mViewModel.getmUserInfo().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                binding.etNewEmail.setText(userModel.getEmail());
            }
        });
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
        binding.btnChangeEmail.setOnClickListener(new View.OnClickListener() {
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
        binding.etNewEmail.setError(null);

        if (binding.etCurrentPassword.getText().toString().isEmpty()){
            binding.etCurrentPassword.setError("Please enter your password");
            isValid = false;
        }
        if (binding.etNewEmail.getText().toString().trim().isEmpty()){
            binding.etNewEmail.setError("Please enter your email");
            isValid = false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etNewEmail.getText().toString().trim()).matches()){
            binding.etNewEmail.setError("Invalid email");
            isValid = false;
        }
        return isValid;
    }
}