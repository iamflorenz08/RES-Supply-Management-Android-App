package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import android.content.Intent;
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

import com.dreambig.supplymanagementapp.Models.SignInModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentForgotPasswordBinding;
import com.dreambig.supplymanagementapp.databinding.FragmentSignInBinding;

import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mViewModel;
    private AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(SignInFragment.this).get(SignInViewModel.class);

        //set status bar
        setStatusBar();

        //Sign In listener;
        signInListener();

        //Forgot Password Listener
        forgotPasswordListener();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();

    }



    private void signInListener() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();

                    alertDialog.show();
                    mViewModel.getIsLoginSuccess(new SignInModel(email,password)).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isSuccess) {
                            binding.btnSignIn.setEnabled(true);
                            alertDialog.dismiss();
                            if(isSuccess){
                                Navigation.findNavController(view).setGraph(R.navigation.main_navigation);
                            }
                            else{
                                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    private boolean isValidated() {
        boolean isValid = true;
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if(binding.etEmail.getText().toString().trim().isEmpty()){
            binding.tilEmail.setError("Input your email");
            isValid = false;
        }
        else if(!isEmailFormatValid(binding.etEmail.getText().toString())){
            binding.tilEmail.setError("Invalid email");
            isValid = false;
        }

        if(binding.etPassword.getText().toString().isEmpty()){
            binding.tilPassword.setError("Input your password");
            isValid = false;
        }

        return isValid;
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.secondary));
    }
    private Boolean isEmailFormatValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void forgotPasswordListener() {
        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotPasswordFragment);
            }
        });
    }

}