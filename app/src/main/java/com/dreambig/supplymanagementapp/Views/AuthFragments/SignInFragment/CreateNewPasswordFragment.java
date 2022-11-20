package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentCreateNewPasswordBinding;


public class CreateNewPasswordFragment extends Fragment {

    private FragmentCreateNewPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewPasswordBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set status  bar
        setStatusBar();
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }
}