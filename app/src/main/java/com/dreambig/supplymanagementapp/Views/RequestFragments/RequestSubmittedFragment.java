package com.dreambig.supplymanagementapp.Views.RequestFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.MainViewModel;
import com.dreambig.supplymanagementapp.Views.NotificationFragments.NotificationViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentRequestSubmittedBinding;

public class RequestSubmittedFragment extends Fragment {

    private FragmentRequestSubmittedBinding binding;
    private MainViewModel sharedViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestSubmittedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        sharedViewModel.getLiveAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                sharedViewModel.loadNotification(userModel.get_id());
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_requestSubmittedFragment_to_chooseFragment);
            }
        });
    }
}