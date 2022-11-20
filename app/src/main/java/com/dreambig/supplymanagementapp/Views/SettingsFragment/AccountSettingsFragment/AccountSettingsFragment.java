package com.dreambig.supplymanagementapp.Views.SettingsFragment.AccountSettingsFragment;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentAccountSettingsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountSettingsFragment extends Fragment {

    private AccountSettingsViewModel mViewModel;
    private FragmentAccountSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);

        //set status bar
        setStatusBar();

        //initialize details
        mViewModel.init();
        init();

        //back
        backListener();

        binding.btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_changeNameFragment);
            }
        });

        binding.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_changeMobileFragment);
            }
        });

        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_changeEmailFragment);
            }
        });

        binding.btnDepartmentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_changeDepPosFragment);
            }
        });

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_changePasswordFragment);
            }
        });

    }

    private void init() {
        mViewModel.getmUserInfo().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if(userModel == null)
                    return;

                binding.tvName.setText(userModel.getFull_name().getFirst_name().toUpperCase() + " " + userModel.getFull_name().getLast_name().toUpperCase());
                binding.tvPhone.setText(userModel.getMobile_number());
                binding.tvEmail.setText(userModel.getEmail());
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

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }
}