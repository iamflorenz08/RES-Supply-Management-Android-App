package com.dreambig.supplymanagementapp.Views.RequestFragments;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.MainViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentRequestBinding;

public class RequestFragment extends Fragment {

    private RequestViewModel mViewModel;
    private MainViewModel mainViewModel;
    private FragmentRequestBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(binding.requestFragmentContainerView.getId());
        navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                mainViewModel.setFragmentID(navDestination.getId());
            }
        });
        setStatusBar();
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }
}