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

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentCreateAccountBinding;

import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountFragment extends Fragment {

    private FragmentCreateAccountBinding binding;
    private CreateAccountViewModel mViewModel;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View Model
        mViewModel = new ViewModelProvider(CreateAccountFragment.this).get(CreateAccountViewModel.class);
        mViewModel.init();

        //Method to change status bar
        setStatusBar();

        //Navigate and set args
        NavigateToDetails();

        //Back method
        backClickListener();

        //sign in click listener
        signInClickListener();

        //Observe email
        observeEmail();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private void observeEmail() {
        mViewModel.getmCheckEmail().observe(getViewLifecycleOwner(), new Observer<AuthResponseModel>() {
            @Override
            public void onChanged(AuthResponseModel authResponseModel) {
                alertDialog.dismiss();
                if(authResponseModel == null)
                    return;

                if(!authResponseModel.getExist()){
                    Bundle userData = new Bundle();
                    userData.putString("email", binding.etEmail.getText().toString());
                    userData.putString("password",binding.etPassword.getText().toString());
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_createAccountFragment_to_setUpDetailsFragment, userData);
                }
                else{
                    binding.tilEmail.setError("Email already used");
                }
            }
        });
    }

    private void signInClickListener() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_createAccountFragment_to_signInFragment);
            }
        });
    }

    private void backClickListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void NavigateToDetails() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){
                    alertDialog.show();
                    mViewModel.checkEmail(binding.etEmail.getText().toString());
                }
            }
        });
    }

    private Boolean isValidated(){
        Boolean isValid = true;
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if(binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.tilEmail.setError("Please enter your email");
            isValid =  false;
        }
        else if (!isEmailFormatValid(binding.etEmail.getText().toString())){
            binding.tilEmail.setError("Invalid email");
            isValid =  false;
        }

        if(binding.etPassword.getText().toString().length() <= 6){
            binding.tilPassword.setError("Password must be above 6 characters");
            isValid = false;
        }

        return isValid;
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

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
}