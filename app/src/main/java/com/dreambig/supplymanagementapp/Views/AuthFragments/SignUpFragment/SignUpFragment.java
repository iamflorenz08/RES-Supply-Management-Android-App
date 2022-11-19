package com.dreambig.supplymanagementapp.Views.AuthFragments.SignUpFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.MainActivity;
import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentSignUpBinding;
import com.dreambig.supplymanagementapp.databinding.ProgressLoadingBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private MainActivity mainActivity;
    private SignUpViewModel mViewModel;
    private AlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(SignUpFragment.this).get(SignUpViewModel.class);
        mViewModel.init();

        //Get the main activity class
        mainActivity = (MainActivity) getActivity();

        //Method to change status bar
        setStatusBar();

        //Click listener for sign up with Email
        NavigateSignUpWithEmail();

        //Click listener for sign in button
        NavigateSignIn();

        //Implementation of One Tap UI
        GoogleOneTapUI();

        //observer
        googleObserver();


        //Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        dialog = builder.create();


    }

    private void googleObserver() {
        mViewModel.getmSignUpGmail().observe(getViewLifecycleOwner(), new Observer<AuthResponseModel>() {
            @Override
            public void onChanged(AuthResponseModel authResponseModel) {
                dialog.dismiss();
                if (authResponseModel == null){
                    return;
                }

                if(authResponseModel.getExist() && authResponseModel.getisGmail()){
                    Navigation.findNavController(binding.getRoot()).setGraph(R.navigation.main_navigation);
                }
                else if(authResponseModel.getExist() && !authResponseModel.getisGmail()){
                    mainActivity.googleSignOut();
                    Toast.makeText(getContext(), "Email already exist", Toast.LENGTH_SHORT).show();
                }
                else{
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_signUpFragment_to_setUpDetailsFragment);
                }


            }
        });
    }

    private void GoogleOneTapUI() {
        binding.btnContinueWGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.googleSignOut();
                mainActivity.showOneTapUI();
            }
        });

        mainActivity.setGoogleAuthCallBack(new MainActivity.GoogleAuthCallBack() {
            @Override
            public void result(GoogleSignInAccount account) {
                mViewModel.checkGoogleAccount(account.getEmail());
                dialog.show();
            }
        });
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

    private void NavigateSignIn() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });
    }

    private void NavigateSignUpWithEmail() {
        binding.btnSignUpWEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.googleSignOut();
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_createAccountFragment);
            }
        });
    }


}