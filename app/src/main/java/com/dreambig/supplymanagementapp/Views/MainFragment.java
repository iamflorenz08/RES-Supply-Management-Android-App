package com.dreambig.supplymanagementapp.Views;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
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

import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentMainBinding;

import java.util.List;

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
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        mViewModel.init();
        mViewModel.loadUserInfo();

        mViewModel.getLiveAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if(userModel == null) return;;

                if(!userModel.getApproved()){
                    try {
                        navController.navigate(R.id.action_requestFragment_to_pendingFragment);
                    }catch (Exception e){
                        navController.popBackStack();
                        navController.navigate(R.id.pendingFragment);
                    }
                    return;
                }
                else{
                    navController.popBackStack();
                    navController.navigate(R.id.requestFragment);
                }

                mViewModel.loadNotification(userModel.get_id());
            }
        });

        mViewModel.getLiveNotification().observe(getViewLifecycleOwner(), new Observer<List<NotificationModel>>() {
            @Override
            public void onChanged(List<NotificationModel> notificationModels) {
                int notifs = 0;

                for (NotificationModel notification : notificationModels){
                    if(!notification.getRead()){
                        notifs += 1;
                    }
                }
                if(notifs > 0){
                    binding.bottomNavigation.getOrCreateBadge(R.id.notificationFragment).setVisible(true);
                    binding.bottomNavigation.getOrCreateBadge(R.id.notificationFragment).setNumber(notifs);
                }
                else{
                    binding.bottomNavigation.getOrCreateBadge(R.id.notificationFragment).setVisible(false);
                }

            }
        });


        mViewModel.getFragmentID().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer fragmentID) {
                if(fragmentID == R.id.addedItemsFragment || fragmentID == R.id.requestHistoryFragment || fragmentID == R.id.requisitionDetailsFragment || fragmentID == R.id.requisitionDetailsFragment2 || fragmentID == R.id.requestSubmittedFragment)
                    binding.bottomNavigation.setVisibility(View.INVISIBLE);
                else
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() != R.id.requestFragment
                        && navDestination.getId() != R.id.notificationFragment
                        && navDestination.getId() != R.id.settingFragment)
                    binding.bottomNavigation.setVisibility(View.GONE);
                else
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });


    }



}