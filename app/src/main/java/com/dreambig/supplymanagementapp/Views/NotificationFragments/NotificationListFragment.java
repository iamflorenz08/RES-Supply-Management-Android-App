package com.dreambig.supplymanagementapp.Views.NotificationFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Adapters.NotificationAdapter;
import com.dreambig.supplymanagementapp.Models.NotificationModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.Views.MainViewModel;
import com.dreambig.supplymanagementapp.databinding.FragmentNotificationListBinding;

import java.util.List;

public class NotificationListFragment extends Fragment {

    private FragmentNotificationListBinding binding;
    private NotificationViewModel sharedViewModel;
    private MainViewModel mainSharedViewModel;
    private NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        mainSharedViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        notificationAdapter = new NotificationAdapter();
        binding.rvNotification.setAdapter(notificationAdapter);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvNotification.setNestedScrollingEnabled(false);
        binding.refresher.setColorSchemeColors(getResources().getColor(R.color.primary));
        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedViewModel.loadNotifications();
            }
        });
        notificationAdapter.setNotificationListener(new NotificationAdapter.NotificationListener() {
            @Override
            public void openNotification(NotificationModel notificationModel) {
                mainSharedViewModel.setRequest(notificationModel.getRequest());
                Navigation.findNavController(view).navigate(R.id.action_notificationListFragment_to_requisitionDetailsFragment);

                if(!notificationModel.getRead()){
                    sharedViewModel.readNotification(notificationModel.get_id());
                }

            }
        });

        sharedViewModel.getLiveNotifications().observe(getViewLifecycleOwner(), new Observer<List<NotificationModel>>() {
            @Override
            public void onChanged(List<NotificationModel> notificationModels) {
                displayNotifications(notificationModels);
                binding.refresher.setRefreshing(false);
            }
        });
    }

    private void displayNotifications(List<NotificationModel> notificationModels) {
        notificationAdapter.setNotifications(notificationModels);
    }
}