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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.MainActivity;
import com.dreambig.supplymanagementapp.Models.FullNameModel;
import com.dreambig.supplymanagementapp.Models.PasswordModel;
import com.dreambig.supplymanagementapp.Models.UserModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.FragmentSetUpDetailsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SetUpDetailsFragment extends Fragment {
    private GoogleSignInAccount account;
    private FragmentSetUpDetailsBinding binding;
    private MainActivity mainActivity;
    private SetUpViewModel mViewModel;
    private Bundle userData;
    private Observer<Boolean> ObserveAccountCreation;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSetUpDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View model
        mViewModel = new ViewModelProvider(SetUpDetailsFragment.this).get(SetUpViewModel.class);

        //Get Arguments
        userData = getArguments();

        //Get main activity class
        mainActivity = (MainActivity) getActivity();

        //Change the status bar
        setStatusBar();

        //Populate the drop down menu
        populateDepartment();

        //initialize details
        initDetails();

        //Complete click listener
        completeClickListener();

        //Back click listener
        backListener();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }


    private void backListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.popBackStack();
            }
        });
    }

    private void completeClickListener() {
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){

                    FullNameModel fullName = new FullNameModel(
                            binding.etFirstName.getText().toString(),
                            binding.etLastName.getText().toString(),
                            binding.etMiddleName.getText().toString()
                    );

                    PasswordModel password = new PasswordModel(
                            null,
                            true);

                    UserModel user = new UserModel(
                            null,
                            password,
                            null,
                            fullName,
                            binding.etMobileNumber.getText().toString(),
                            binding.atvDepartment.getText().toString(),
                            binding.etPosition.getText().toString()
                    );

                    if(userData != null){
                        user.setEmail(userData.getString("email"));
                        user.setPhoto_URL(null);
                        password.setPassword(userData.getString("password"));
                        password.setGmail(false);
                    }
                    else{
                        user.setEmail(account.getEmail());
                        if (account.getPhotoUrl() != null)
                            user.setPhoto_URL(account.getPhotoUrl().toString());
                    }

                    binding.btnComplete.setEnabled(false);
                    alertDialog.show();
                    ObserveAccountCreation = new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isSuccess) {
                            alertDialog.dismiss();
                            binding.btnComplete.setEnabled(true);
                            if(isSuccess){
                                Navigation.findNavController(binding.getRoot()).setGraph(R.navigation.main_navigation);
                            }
                            else{
                                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    mViewModel.getIsCreateSuccess(user).observe(getViewLifecycleOwner(), ObserveAccountCreation);
                }
            }
        });


    }

    private boolean isValidated() {
        Boolean isValid = true;
        binding.tilFirstName.setError(null);
        binding.tilLastName.setError(null);
        binding.tilMobileNumber.setError(null);
        binding.tilDepartment.setError(null);
        binding.tilPosition.setError(null);

        if (binding.etFirstName.getText().toString().trim().isEmpty()){
            binding.tilFirstName.setError("First name cannot be empty");
            isValid = false;
        }

        if (binding.etLastName.getText().toString().trim().isEmpty()){
            binding.tilLastName.setError("Last name cannot be empty");
            isValid = false;
        }

        if (binding.etMobileNumber.getText().toString().trim().isEmpty()){
            binding.tilMobileNumber.setError("Please enter your number");
            isValid = false;
        }
        else if(binding.etMobileNumber.getText().toString().length() != 11){
            binding.tilMobileNumber.setError("Invalid number");
            isValid = false;
        }

        if (binding.atvDepartment.getText().toString().equals("Select")){
            binding.tilDepartment.setError("Please select your department");
            isValid = false;
        }

        if (binding.etPosition.getText().toString().trim().isEmpty()){
            binding.tilPosition.setError("Please enter your position");
            isValid = false;
        }

        return isValid;
    }

    private void initDetails() {
        if(account != null){
            binding.etFirstName.setText(account.getGivenName());
            binding.etLastName.setText(account.getFamilyName());
        }
    }

    private void setStatusBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getActivity().getWindow(), getActivity().getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }

    private void populateDepartment() {
        String[] departmentLists = {"Select", "Kindergarten", "Grade 1", "Grade 2",
                "Grade3", "Grade 4", "Grade 5", "Grade 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, departmentLists);
        binding.atvDepartment.setAdapter(adapter);
        binding.atvDepartment.setText(adapter.getItem(0),false);
    }

}