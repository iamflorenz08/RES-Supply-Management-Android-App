package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentForgotPasswordBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private SignInViewModel mViewModel;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        setStatusBar();
        
        backListener();

        submitListener();
    }
    
    private void setStatusBar(){
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }
    
    private void backListener () {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void submitListener () {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resetPassword()){
                    mViewModel.sendEmail(binding.etEmail.getText().toString());
                    binding.tilEmail.setHelperText("Password recovery sent to your email.");
                    binding.tilEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
                    binding.btnSubmit.setEnabled(false);
                };
            }
        });
    }

    private Boolean resetPassword(){
        String email = binding.etEmail.getText().toString().trim();

        if (email.isEmpty()){
            binding.tilEmail.setError("Please enter your email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tilEmail.setError("Please enter a valid email");
            return false;
        }

        return true;
    }
}
