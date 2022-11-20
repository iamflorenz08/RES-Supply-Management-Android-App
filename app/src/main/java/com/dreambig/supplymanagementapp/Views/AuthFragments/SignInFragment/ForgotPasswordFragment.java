package com.dreambig.supplymanagementapp.Views.AuthFragments.SignInFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.renderscript.ScriptGroup;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.MainActivity;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentForgotPasswordBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWebException;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

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

        backListener();

        submitListener();
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
                resetPassword();
            }
        });
    }

    private  void resetPassword(){
        String email = binding.etEmail.getText().toString().trim();
        if (email.isEmpty()){
            binding.etEmail.setError("Please enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("Please enter a valid email");
            return;
        }
    }
}