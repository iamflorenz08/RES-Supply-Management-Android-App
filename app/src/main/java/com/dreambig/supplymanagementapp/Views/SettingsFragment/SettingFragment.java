package com.dreambig.supplymanagementapp.Views.SettingsFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dreambig.supplymanagementapp.MainActivity;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentSettingBinding;
import com.github.file_picker.FilePicker;

import java.io.File;
import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;
import id.zelory.compressor.Compressor;

@AndroidEntryPoint
public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    private FragmentSettingBinding binding;
    private MainActivity mainActivity;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        mViewModel.init();

        //main activity
        mainActivity = (MainActivity) getActivity();

        //sets the status bar
        setStatusBar();

        //user live data
        liveDataUserInfo();

        //Account Settings
        accountSettings();

        //Notification settings
        notificationSettings();

        //Log out
        logOut();

        //image picker
        imagePicker();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private void imagePicker() {

        binding.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilePicker.Builder(mainActivity)
                        .setLimitItemSelection(1)
                        .setAccentColor(getResources().getColor(R.color.primary))
                        .setCancellable(true)
                        .setGridSpanCount(3)
                        .setSubmitText("Apply")
                        .setOnSubmitClickListener(files -> {
                            // Do something here with selected files
                            Uri uri = Uri.parse("file://" + files.get(0).getFile().toString());

                            if(!mViewModel.getUser().getValue().getPassword().getGmail()){
                                String prevPhotoUrl =  mViewModel.getUser().getValue().getPhoto_URL();

                                if(prevPhotoUrl != null){
                                    int startIndex = 97;
                                    int endIndex = prevPhotoUrl.indexOf("?alt=media&token=");
                                    mViewModel.deletePhoto(prevPhotoUrl.substring(startIndex,endIndex));
                                }
                            }

                            try {
                                File compressor = new Compressor(getContext()).compressToFile(new File(uri.getPath()));
                                mViewModel.uploadProfilePhoto(Uri.fromFile(compressor));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            alertDialog.show();
                        })
                        .setOnItemClickListener((media, pos, adapter) -> {
                            if (!media.getFile().isDirectory()) {
                                adapter.setSelected(pos);
                            }
                        })
                        .buildAndShow();
            }
        });

        mViewModel.getmProfileUri().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String photo_URL) {
                if(photo_URL == null)
                    return;

                mViewModel.updatePhoto(photo_URL);
            }
        });

        mViewModel.getIsUpdatePhotoSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if(isSuccess == null)
                    return;

                if(isSuccess){
                    mViewModel.loadUserInfo();
                    Toast.makeText(getContext(), "Upload success.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(getContext(), "Upload failed.", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });
    }



    private void logOut() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.googleSignOut();
                mainActivity.signOut();
            }
        });
    }

    private void notificationSettings() {
        binding.btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_notificationSettingsFragment);
            }
        });
    }

    private void accountSettings() {
        binding.btnAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_accountSettingsFragment);
            }
        });
    }

    private void liveDataUserInfo() {
        binding.tvFullName.setText(null);
        binding.tvEmail.setText(null);

        mViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if(userModel != null){
                    Glide.with(binding.getRoot()).load(userModel.getPhoto_URL()).circleCrop().placeholder(getResources().getDrawable(R.drawable.user_image)).into(binding.ivProfilePhoto);
                    binding.tvFullName.setText(userModel.getFull_name().getFirst_name() + " " + userModel.getFull_name().getLast_name());
                    binding.tvEmail.setText(userModel.getEmail());
                }

                alertDialog.dismiss();
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