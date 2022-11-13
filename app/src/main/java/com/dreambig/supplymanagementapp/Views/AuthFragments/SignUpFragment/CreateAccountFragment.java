package com.dreambig.supplymanagementapp.Views.AuthFragments.SignUpFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentCreateAccountBinding;

import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountFragment extends Fragment {

    private FragmentCreateAccountBinding binding;
    private CreateAccountViewModel mViewModel;
    private Observer<CheckAccountModel> oCheckAccount;

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

        //Method to change status bar
        setStatusBar();

        //Navigate and set args
        NavigateToDetails();

        //Back method
        backClickListener();

        //sign in click listener
        signInClickListener();
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
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void NavigateToDetails() {

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){
                    String email = binding.etEmail.getText().toString();
                    String password =  binding.etPassword.getText().toString();

                    oCheckAccount = new Observer<CheckAccountModel>() {
                        @Override
                        public void onChanged(CheckAccountModel checkAccountModel) {
                            if(checkAccountModel.getExist()){
                                binding.tilEmail.setError("Email already used");
                                return;
                            }

                            Bundle userData = new Bundle();
                            userData.putString("email", email);
                            userData.putString("password",password);
                            Navigation.findNavController(view).navigate(R.id.action_createAccountFragment_to_setUpDetailsFragment, userData);
                        }
                    };

                    mViewModel.getmCheckAccount(email).observe(getViewLifecycleOwner(), oCheckAccount);

                }
            }
        });
    }

    private Boolean isValidated(){
        Boolean isValid = true;
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if(binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.tilEmail.setError("Input your email");
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