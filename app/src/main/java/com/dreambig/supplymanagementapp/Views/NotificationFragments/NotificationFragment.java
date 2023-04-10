package com.dreambig.supplymanagementapp.Views.NotificationFragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.MainViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentNotificationBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    private MainViewModel mainViewModel;
    private FragmentNotificationBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mViewModel.init();
        mViewModel.loadNotifications();
        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(binding.fragmentContainer.getId());
        navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                mainViewModel.setFragmentID(navDestination.getId());
                if(navDestination.getId() == R.id.requisitionDetailsFragment2){
                    binding.notificationHeader.setVisibility(View.GONE);
                }
                else{
                    binding.notificationHeader.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}