package com.dreambig.supplymanagementapp.Views;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.SettingsFragment.SettingFragment;
import com.dreambig.supplymanagementapp.databinding.FragmentMainBinding;

import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment{

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.loadUserInfo();
        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() != R.id.stockFragment
                        && navDestination.getId() != R.id.borrowsFragment
                        && navDestination.getId() != R.id.notificationFragment
                        && navDestination.getId() != R.id.settingFragment)
                    binding.bottomNavigation.setVisibility(View.INVISIBLE);
                else
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
    }



}