package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.Locals.Token;
import com.dreambig.supplymanagementapp.Models.ResponseModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentCreateNewPasswordBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateNewPasswordFragment extends Fragment {

    private FragmentCreateNewPasswordBinding binding;
    private Bundle bundle;
    private String token;
    private SignInViewModel mViewModel;
    private AlertDialog alertDialog;

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
        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        mViewModel.init();

        //initialize details
        init();

        //set status  bar
        setStatusBar();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPasswordValid()){
                    mViewModel.resetPassword(token, binding.etRepeatPassword.getText().toString());
                    alertDialog.show();
                }

            }
        });

        mViewModel.getResponseResetPassword().observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                alertDialog.dismiss();
                if (responseModel == null)
                    return;

                if(!responseModel.getError()){
                    Toast.makeText(getContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                    return;
                }

                Toast.makeText(getContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Boolean isPasswordValid() {
        binding.tilPassword.setError(null);
        binding.tilRepeatPassword.setError(null);

        if(binding.etPassword.getText().toString().length() < 6){
            binding.tilPassword.setError("Password must be 6 characters above.");
            return false;
        }

        if(!binding.etPassword.getText().toString().equals(binding.etRepeatPassword.getText().toString())){
            binding.tilRepeatPassword.setError("Password does not match.");

            return false;
        }

        return true;
    }

    private void init(){
        bundle = getArguments();
        token = bundle.getString("resetToken");
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }
}